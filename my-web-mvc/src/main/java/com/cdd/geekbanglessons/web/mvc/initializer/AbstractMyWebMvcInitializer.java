package com.cdd.geekbanglessons.web.mvc.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author yangfengshan
 * @create 2021-03-24 13:52
 **/
public abstract class AbstractMyWebMvcInitializer implements MyWebMvcInitializer {
    private final int ordinal;
    private final String name;

    public int getOrdinal() {
        return ordinal;
    }

    public String getName() {
        return name;
    }

    public AbstractMyWebMvcInitializer(String name, int ordinal) {
        this.ordinal = ordinal;
        this.name = name;
    }

    @Override
    public abstract void onStartup(ServletContext servletContext) throws ServletException;
}
