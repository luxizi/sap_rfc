package com.weimeizi.rfc.function;

import com.alibaba.fastjson.JSON;
import com.sap.conn.jco.*;
import com.weimeizi.rfc.mapper.MaxComputeMapper;
import com.weimeizi.rfc.pojo.ZWmzFm202;
import com.weimeizi.rfc.util.SAPEntityAssembleResolver;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @program: sap_rfc
 * @description: 获取ZWmzFm202函数并插入ods_zwmz_fm_202表中
 * @author: licihui
 * @create: 2023-06-01 11:34
 **/
public class ZWmzFm202Function implements Runnable{

    private MaxComputeMapper mapper = MaxComputeMapper.getInstance();

    @Override
    @SneakyThrows
    public void run() {
        //初始化格式
        DateTimeFormatter ptFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        //1. 从SAP-RFC拿数
        JCoDestination destination = JCoDestinationManager.getDestination("DEFAULT");
        JCoFunction function = destination.getRepository().getFunction("ZWMZ_FM_202");
        //如果输出为值参数则使用getExportParameterList()表值则为getTableParameterList()
        JCoParameterList importParameterList = function.getTableParameterList();
//        JCoTable itWerks = importParameterList.getTable("IT_WERKS");
//        itWerks.appendRow();
//        itWerks.setValue("SIGN", "I");
//        itWerks.setValue("OPTION", "EQ");
//        itWerks.setValue("LOW", "8400");
        JCoTable itBldat = importParameterList.getTable("IT_BUDAT");
        itBldat.appendRow();
        itBldat.setValue("SIGN", "I");
        itBldat.setValue("OPTION", "BT");
        itBldat.setValue("LOW", ptFormatter.format(now.plusDays(-1)));
        itBldat.setValue("HIGH", ptFormatter.format(now));
        function.execute(destination);
        JCoTable output = function.getTableParameterList().getTable("ET_DATA");
        //2. 将数据格式化到我们期望的结构
        SAPEntityAssembleResolver resolver = new SAPEntityAssembleResolver<>(ZWmzFm202.class);
        List<ZWmzFm202> data = resolver.resolve(output);
        //3. 将数据插入到指定的STG表
        System.out.println("插入了：" + data.size() + "条数据");
        System.out.println(JSON.toJSONString(data));
        System.out.println("插入了：" + data.size() + "条数据");
        mapper.overwrite(data);
//        mapper.insert(data);
    }

//    public static void main(String[] args) {
//        System.out.println(ZoneId.getAvailableZoneIds());
//    }
}