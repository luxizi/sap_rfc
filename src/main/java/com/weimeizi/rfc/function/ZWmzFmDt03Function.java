package com.weimeizi.rfc.function;

import com.alibaba.fastjson.JSON;
import com.sap.conn.jco.*;
import com.weimeizi.rfc.mapper.MaxComputeMapper;
import com.weimeizi.rfc.pojo.ZWmzFmDt03;
import com.weimeizi.rfc.util.SAPEntityAssembleResolver;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname DrawBacKSapCustomerTask
 * @Description TODO 抽取客户档案
 * @Date 2021-09-30 12:26
 * @Author by 03126
 */
public class ZWmzFmDt03Function implements Runnable {

    private MaxComputeMapper mapper = MaxComputeMapper.getInstance();

    @Override
    @SneakyThrows
    public void run() {
        //1. 从SAP-RFC拿数（从玄武-API拿数）
        JCoDestination destination = JCoDestinationManager.getDestination("DEFAULT");
        JCoFunction function = destination.getRepository().getFunction("ZWMZ_FM_DT_03");
        function.getImportParameterList().setValue("IV_INPUT", "Z001");
        function.execute(destination);
        //如果输出为值参数则使用getExportParameterList()表值则为getTableParameterList()
        JCoTable output = function.getExportParameterList().getTable("EV_OUTPUT");
        //2. 将数据格式化到我们期望的结构
        SAPEntityAssembleResolver resolver = new SAPEntityAssembleResolver<>(ZWmzFmDt03.class);
        List<ZWmzFmDt03> data = resolver.resolve(output);
        function.getImportParameterList().setValue("IV_INPUT", "Z002");
        function.execute(destination);
        JCoTable output2 = function.getExportParameterList().getTable("EV_OUTPUT");
        List<ZWmzFmDt03> data2 = resolver.resolve(output2);//这里不知道你有没有写错，备注一下给你output2，但是好像不影响结果
        data.addAll(data2);
        //3. 将数据插入到指定的STG表
        System.out.println(JSON.toJSONString(data));
        mapper.overwrite(data);
        System.out.println("插入了：" + data.size() + "条数据");
    }


}
