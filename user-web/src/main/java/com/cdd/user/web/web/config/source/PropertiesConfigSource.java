package com.cdd.user.web.web.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertiesConfigSource implements ConfigSource {

    /**
     * Java 系统属性最好通过本地变量保存，使用 Map 保存，尽可能运行期不去调整
     * -Dapplication.name=user-web
     */
    private final Properties properties = new Properties();

    public PropertiesConfigSource() {
        //获取properties文件
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropertiesConfigSource.class.getClassLoader().getResourceAsStream("META-INF/config/config.properties");
        // 使用properties对象加载输入流
        if (null==in)return;
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet().stream().map(Object::toString).collect(Collectors.toSet());
    }

    @Override
    public String getValue(String propertyName) {
        return properties.getProperty(propertyName);
    }

    @Override
    public String getName() {
        return "config/config.properties";
    }
}
