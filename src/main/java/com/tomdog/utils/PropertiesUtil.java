package com.tomdog.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zhouyu
 * @description 读取配置文件
 **/
public class PropertiesUtil {
    private static final String PROPERTIES_NAME = "/tomdog.properties";
    /**
     * 按key获取值
     * @param key
     * @return
     */
    public static String readProperty(String key) {
        String value = "";
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getResourceAsStream(PROPERTIES_NAME);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }


}
