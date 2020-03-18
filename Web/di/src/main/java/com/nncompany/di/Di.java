package com.nncompany.di;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Di {

    private static Di instance;

    public static Di getInstance(){
        if (instance == null){
            instance = new Di();
        }
        return instance;
    }

    private Map<Class<?>, Object> map = new HashMap<>();
    private static final Logger log = Logger.getLogger(Di.class);
    private String PROPERTY_PATH = "/DI.properties";

    public void setLog4jPropertiesFilePath(String log4jPropertiesFilePath){
        PropertyConfigurator.configure(log4jPropertiesFilePath);
    }

    public <T> T load(Class<T> interf) {
        log.info("Start loading class" + interf.getName());
        try {
            if(map.containsKey(interf)){
                return (T) map.get(interf);
            } else {
                String className =  getProperty(PROPERTY_PATH,interf.getName());
                Class clazz = Class.forName(className);
                T tObj = (T)clazz.newInstance();
                map.put(interf, tObj);
                return tObj;
            }
        } catch ( ClassNotFoundException
                | IllegalAccessException
                | InstantiationException e) {

           log.error("Cant't load class "+ e);
        }
        return null;
    }

    private String getProperty(String propertyPath, String propertyName){
        log.info("Starting get property");
        String propertyValye = "";
        Properties properties = new Properties();
        try (InputStream inputStream = this.getClass().getResourceAsStream(propertyPath)){
            properties.load(inputStream);
            propertyValye = properties.getProperty(propertyName);
        } catch (Exception e) {
            log.error("Can't get property " + e);
        }
        return propertyValye;
    }
}
