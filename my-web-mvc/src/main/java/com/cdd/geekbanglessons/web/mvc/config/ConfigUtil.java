package com.cdd.geekbanglessons.web.mvc.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

/**
 * @author yangfengshan
 * @create 2021-03-24 17:13
 **/
public class ConfigUtil {
    private static ThreadLocal<Config> configThreadLocal = new ThreadLocal<>();

    public static Config getConfig(ClassLoader classLoader) {
        //在配置初始化中放入了
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        return configProviderResolver.getConfig(classLoader);
    }

    public static Config getConfigThreadLocal() {
        return configThreadLocal.get();
    }

    public static void setConfigThreadLocal(Config config) {
        configThreadLocal.set(config);
    }

    public static void removeConfigThreadLocal(Config config) {
        configThreadLocal.remove();
    }
}
