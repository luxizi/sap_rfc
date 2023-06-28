package com.luxizi.rfc.config;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Properties;

/**
 * @Classname DesinationProvider
 * @Description TODO
 * @Date 2021/2/6 11:26
 * @Author by 03126
 */
@Component
public class DestinationProvider implements DestinationDataProvider {

    private DestinationDataEventListener el;
    /**
     * K:DestinationName,V:Properties
     */
    private static HashMap<String,Properties> destinations = new HashMap<>();
    private static DestinationProvider provider = new DestinationProvider();
    private static String ASHOST = "192.168.1.104";
    private static String SYSNR = "02";
    private static String CLIENT = "800";
    private static String USER = "ZIFUSER";
    private static String PASSWD = "Passw0rd";
    private static String LANG = "EN";
    private static String PEAK_LIMIT = "10";
    private static String POOL_CAPACITY = "3";

    static {
        Properties properties = new Properties();
        properties.setProperty(DestinationDataProvider.JCO_ASHOST,ASHOST);
        properties.setProperty(DestinationDataProvider.JCO_SYSNR,SYSNR);
        properties.setProperty(DestinationDataProvider.JCO_CLIENT,CLIENT);
        properties.setProperty(DestinationDataProvider.JCO_USER,USER);
        properties.setProperty(DestinationDataProvider.JCO_PASSWD,PASSWD);
        properties.setProperty(DestinationDataProvider.JCO_LANG,LANG);
        properties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,PEAK_LIMIT);
        properties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY,POOL_CAPACITY);
        destinations.put("DEFAULT",properties);
        Environment.registerDestinationDataProvider(DestinationProvider.getInstance());
    }

    @Override
    public Properties getDestinationProperties(String destinationName) {
        if (destinations.containsKey(destinationName)){
            return destinations.get(destinationName);
        }else {
            throw new RuntimeException("Destination:" + destinationName +"is not available");
        }
    }

    @Override
    public boolean supportsEvents() {
        return true;
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener destinationDataEventListener) {
        this.el = destinationDataEventListener;
    }

    private DestinationProvider() { //private修饰，单例模式
        if (provider==null){
            destinations = new HashMap<>();
        }
    }

    public static DestinationProvider getInstance(){
        return provider;
    }

    public void addDestination(String destinationName,Properties properties){
        synchronized (provider){
            destinations.put(destinationName,properties);
        }
    }
}
