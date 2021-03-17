package com.cdd.user.web.web.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author yangfengshan
 * @create 2021-03-17 10:36
 * @date 2021-03-17 11:27
 **/
public class StringToIntegerConverter implements Converter {
    @Override
    public Integer convert(String s) throws IllegalArgumentException, NullPointerException {
        return Integer.valueOf(s);
    }
}
