package com.luxizi.rfc.function;

import com.alibaba.fastjson.JSON;
import com.luxizi.rfc.mapper.MaxComputeMapper;
import com.luxizi.rfc.pojo.ZWMZFM176;
import com.luxizi.rfc.util.SAPEntityAssembleResolver;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @program: sap_rfc
 * @description: 基于批次库存的账龄报表Function
 * @author: licihui
 * @create: 2023-06-01 13:56
 **/
public class ZWmzFm176Function implements Runnable{
    private MaxComputeMapper mapper = MaxComputeMapper.getInstance();

    @Override
    @SneakyThrows
    public void run() {
        //1. 从SAP-RFC拿数（从玄武-API拿数）
        JCoDestination destination = JCoDestinationManager.getDestination("DEFAULT");
        JCoFunction function = destination.getRepository().getFunction("ZWMZ_FM_176");
        LocalDate yesterday = LocalDate.now().plusDays(-1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String param = formatter.format(yesterday);
        function.getImportParameterList().setValue("IM_DATE", param);
        function.execute(destination);
        //如果输出为值参数则使用getExportParameterList()表值则为getTableParameterList()
        JCoTable output = function.getTableParameterList().getTable("GT_OUTPUT");
        //2. 将数据格式化到我们期望的结构
        SAPEntityAssembleResolver resolver = new SAPEntityAssembleResolver<>(ZWMZFM176.class);
        List<ZWMZFM176> data = resolver.resolve(output);
        //3. 将数据插入到指定的STG表
        System.out.println("插入了：" + data.size() + "条数据");
        System.out.println(JSON.toJSONString(data));
        mapper.overwrite(data);
    }
}