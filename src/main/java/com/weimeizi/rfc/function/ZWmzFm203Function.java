package com.weimeizi.rfc.function;

import com.alibaba.fastjson.JSON;
import com.sap.conn.jco.*;
import com.weimeizi.rfc.mapper.MaxComputeMapper;
import com.weimeizi.rfc.pojo.ZWmzFm203;
import com.weimeizi.rfc.util.SAPEntityAssembleResolver;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @program: sap_rfc
 * @description: 公司调拨查询(STO销售与采购)
 * @author: licihui
 * @create: 2023-06-07 12:14
 **/
public class ZWmzFm203Function implements Runnable{

    private MaxComputeMapper mapper = MaxComputeMapper.getInstance();
    @Override
    @SneakyThrows
    public void run() {
        //初始化格式
        DateTimeFormatter ptFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        //1. 从SAP-RFC拿数
        JCoDestination destination = JCoDestinationManager.getDestination("DEFAULT");
        JCoFunction function = destination.getRepository().getFunction("ZWMZ_FM_203");
        //如果输出为值参数则使用getExportParameterList()表值则为getTableParameterList()
        JCoParameterList importParameterList = function.getTableParameterList();
        JCoTable budat = importParameterList.getTable("IT_BUDAT");
        budat.appendRow();
        budat.setValue("SIGN", "I");
        budat.setValue("OPTION", "BT");
        budat.setValue("LOW", ptFormatter.format(now.plusDays(-1)));
        budat.setValue("HIGH", ptFormatter.format(now));
//        JCoTable ebeln = importParameterList.getTable("IT_EBELN");
//        ebeln.appendRow();
//        ebeln.setValue("SIGN", "I");
//        ebeln.setValue("OPTION", "EQ");
//        ebeln.setValue("LOW", "5700000209");
//        JCoTable bukrs = importParameterList.getTable("IT_BUKRS");
//        bukrs.appendRow();
//        bukrs.setValue("SIGN", "I");
//        bukrs.setValue("OPTION", "EQ");
//        bukrs.setValue("LOW", "9000");
        function.execute(destination);
        JCoTable output = function.getTableParameterList().getTable("ET_DATA");
        //2. 将数据格式化到我们期望的结构
        SAPEntityAssembleResolver resolver = new SAPEntityAssembleResolver<>(ZWmzFm203.class);
        List<ZWmzFm203> data = resolver.resolve(output);
        //3. 将数据插入到指定的STG表
        System.out.println("插入了：" + data.size() + "条数据");
        System.out.println(JSON.toJSONString(data));
        mapper.overwrite(data);
        System.out.println("插入了：" + data.size() + "条数据");
        System.out.println(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("当前时间为："+formatter.format(now));

    }
}