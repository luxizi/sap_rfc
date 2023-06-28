package com.weimeizi.rfc.function;

import com.alibaba.fastjson.JSON;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import com.weimeizi.rfc.mapper.MaxComputeMapper;
import com.weimeizi.rfc.pojo.ZWMZFMDT05;
import com.weimeizi.rfc.util.SAPEntityAssembleResolver;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @program: sap_rfc
 * @description: BDP产品库存数据导出接口Function 库存快照
 * @author: licihui
 * @create: 2023-06-01 13:56
 **/
public class ZWmzFmDt05Function implements Runnable{
    private MaxComputeMapper mapper = MaxComputeMapper.getInstance();

    @Override
    @SneakyThrows
    public void run() {
        //1. 从SAP-RFC拿数
        JCoDestination destination = JCoDestinationManager.getDestination("DEFAULT");
        JCoFunction function = destination.getRepository().getFunction("ZWMZ_FM_DT_05");
        function.getImportParameterList().setValue("IV_INPUT", "Z001");
        function.execute(destination);
        //如果输出为值参数则使用getExportParameterList()表值则为getTableParameterList()
        JCoTable output = function.getExportParameterList().getTable("EV_OUTPUT");
        //2. 将数据格式化到我们期望的结构
        SAPEntityAssembleResolver resolver = new SAPEntityAssembleResolver<>(ZWMZFMDT05.class);
        List<ZWMZFMDT05> data = resolver.resolve(output);
        //3. 将数据插入到指定的STG表
        System.out.println("插入了：" + data.size() + "条数据");
        System.out.println(JSON.toJSONString(data));
        mapper.overwrite(data);

    }
}
