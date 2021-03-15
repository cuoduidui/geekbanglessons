package com.cdd.user.web.web.Mbean;

import com.cdd.geekbanglessons.web.mvc.context.ComponentContext;

import java.util.stream.Collectors;

/**
 * @author yangfengshan
 * @create 2021-03-15 18:52
 **/
public class WebContext implements WebContextMBean {
    private ComponentContext componentContext;
    private String appName;

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public ComponentContext getComponentContext() {
        return ComponentContext.getInstance();
    }

    @Override
    public String toString() {
        return "WebContext{}:" + ComponentContext.getInstance().getComponentNames().stream().collect(Collectors.joining(","));
    }
}
