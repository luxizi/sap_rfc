package com.luxizi.rfc.function;

import com.alibaba.fastjson.JSON;
import com.luxizi.rfc.mapper.MaxComputeMapper;
import com.luxizi.rfc.pojo.ZWmzFm204;
import com.luxizi.rfc.util.SAPEntityAssembleResolver;
import com.sap.conn.jco.*;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @program: sap_rfc
 * @description: 销售开票及成本明细查询
 * @author: licihui
 * @create: 2023-06-07 12:14
 **/
public class ZWmzFm204Function implements Runnable{
    private MaxComputeMapper mapper = MaxComputeMapper.getInstance();

    @Override
    @SneakyThrows
    public void run() {
        //初始化格式
        DateTimeFormatter ptFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        //1. 从SAP-RFC拿数
        JCoDestination destination = JCoDestinationManager.getDestination("DEFAULT");
        JCoFunction function = destination.getRepository().getFunction("ZWMZ_FM_204");
        //如果输出为值参数则使用getExportParameterList()表值则为getTableParameterList()
        JCoParameterList importParameterList = function.getTableParameterList();
//        JCoTable itVbeln = importParameterList.getTable("IT_VBELN");
//        itVbeln.appendRow();
//        itVbeln.setValue("SIGN", "I");
//        itVbeln.setValue("OPTION", "EQ");
//        itVbeln.setValue("LOW", "3100190122");
        JCoTable itfkdat = importParameterList.getTable("IT_FKDAT");
        itfkdat.appendRow();
        itfkdat.setValue("SIGN", "I");
        itfkdat.setValue("OPTION", "BT");
        itfkdat.setValue("LOW", ptFormatter.format(now.plusDays(-1)));
        itfkdat.setValue("HIGH", ptFormatter.format(now));
        function.execute(destination);
        JCoTable output = function.getTableParameterList().getTable("ET_DATA");
        //2. 将数据格式化到我们期望的结构
        SAPEntityAssembleResolver resolver = new SAPEntityAssembleResolver<>(ZWmzFm204.class);
        List<ZWmzFm204> data = resolver.resolve(output);
        //3. 将数据插入到指定的STG表
        System.out.println("插入了：" + data.size() + "条数据");
        System.out.println(JSON.toJSONString(data));
        mapper.overwrite(data);
//        mapper.insert(data);
        System.out.println("插入了：" + data.size() + "条数据");
//        System.out.println(ZoneId.systemDefault());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//        System.out.println("当前时间为："+formatter.format(now));

    }
}