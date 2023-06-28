package com.luxizi.rfc.config;

/**
 * @Classname AliyunConfig
 * @Description
 * @Date 2022-01-17 16:18
 * @Author by 03800
 */
public class AliYunConfig {

    //"<your access id>";
    public final static String ACCESS_ID = "################";
    ///"<your access Key>";
    public final static String ACCESS_KEY = "###############";
    public final static String ODPS_URL = "https://service.odps.aliyun.com/api";
    //华南1深圳经典网络Tunnel Endpoint
    public final static String TUNNEL_URL = "https://dt.cn-shenzhen.maxcompute.aliyun.com";
    //默认情况下，使用公网进行传输。如果需要通过内网进行数据传输，必须设置tunnelUrl变量。
    public static String ODPS_END_POINT = "https://service.cn-shenzhen.maxcompute.aliyun.com/api";
    public static String TUNNEL_END_POINT = "<tunnel_endpoint>";
    public final static String PROJECT_DEV = "#############";
    public final static String PROJECT_PRD = "#############";
    public final static String ODS_SFA_VISIT_RECORD = "###############";
    //ODPS驱动类
    public static final String ODPS_DRIVER_NAME = "com.aliyun.odps.jdbc.OdpsDriver";
    public static final String ODPS_WMZ_DATA_WAREHOUSE_URL = "########################";
}
