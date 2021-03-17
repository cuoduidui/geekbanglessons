package com.cdd.geekbanglessons.web.mvc.configuration.microprofile.config;


import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.Method;
import java.util.*;

public class JavaConfig implements Config {

    /**
     * 内部可变的集合，不要直接暴露在外面
     */
    private List<ConfigSource> configSources = new LinkedList<>();
    /**
     * String转换器
     */
    private Map<Class, Converter> converterMap = new HashMap();
    private static Comparator<ConfigSource> configSourceComparator = new Comparator<ConfigSource>() {
        @Override
        public int compare(ConfigSource o1, ConfigSource o2) {
            return Integer.compare(o2.getOrdinal(), o1.getOrdinal());
        }
    };

    public JavaConfig() {
        ClassLoader classLoader = getClass().getClassLoader();
        ServiceLoader<ConfigSource> configSourceServiceLoader = ServiceLoader.load(ConfigSource.class, classLoader);
        configSourceServiceLoader.forEach(configSources::add);
        // 排序
        configSources.sort(configSourceComparator);
        ServiceLoader<Converter> converterServiceLoader = ServiceLoader.load(Converter.class, classLoader);
        converterServiceLoader.forEach((converter) -> {
            try {
                Method method = converter.getClass().getMethod("convert", String.class);
                Class<?> clazz = method.getReturnType();
                converterMap.put(clazz, converter);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        String propertyValue = getPropertyValue(propertyName);
        if (propertyType == String.class) return (T) propertyValue;
        if (StringUtils.isEmpty(propertyValue)) return null;
        // String 转换成目标类型
        Converter converter = converterMap.get(propertyType);
        T value = (T) converter.convert(propertyValue);
        return value;
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        return null;
    }

    protected String getPropertyValue(String propertyName) {
        String propertyValue = null;
        for (ConfigSource configSource : configSources) {
            propertyValue = configSource.getValue(propertyName);
            if (propertyValue != null) {
                break;
            }
        }
        return propertyValue;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        T value = getValue(propertyName, propertyType);
        return Optional.ofNullable(value);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return null;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return Collections.unmodifiableList(configSources);
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        return Optional.empty();
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
