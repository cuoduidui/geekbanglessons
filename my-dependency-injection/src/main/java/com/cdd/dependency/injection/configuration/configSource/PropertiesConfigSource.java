package com.cdd.dependency.injection.configuration.configSource;

import com.cdd.geekbanglessons.configuration.microprofile.config.source.MapBasedConfigSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigSource extends MapBasedConfigSource {

    public PropertiesConfigSource() {
        super("PropertiesConfigSource", 400);
    }

    @Override
    public void prepareConfigData(Map configData) throws Throwable {
        Properties properties = new Properties();
        //获取properties文件
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropertiesConfigSource.class.getClassLoader().getResourceAsStream("META-INF/config/config.properties");
        // 使用properties对象加载输入流
        if (null == in) return;
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configData.putAll(properties);
    }
}
