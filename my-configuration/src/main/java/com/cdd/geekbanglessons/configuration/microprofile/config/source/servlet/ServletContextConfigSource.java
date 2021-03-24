package com.cdd.geekbanglessons.configuration.microprofile.config.source.servlet;

import com.cdd.geekbanglessons.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

public class ServletContextConfigSource extends MapBasedConfigSource {

    private final ServletContext servletContext;

    public ServletContextConfigSource(ServletContext servletContext) {
        super("ServletContext Init Parameters", 500);
        this.servletContext = servletContext;
        super.getProperties();
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        if (servletContext == null) return;
        Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            configData.put(parameterName, servletContext.getInitParameter(parameterName));
        }
    }
}
