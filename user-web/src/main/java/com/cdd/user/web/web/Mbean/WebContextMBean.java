package com.cdd.user.web.web.Mbean;

import com.cdd.geekbanglessons.web.mvc.context.ComponentContext;

/**
 * @author yangfengshan
 * @create 2021-03-15 18:44
 **/
public interface WebContextMBean {
    ComponentContext getComponentContext();

    @Override
    String toString();

    String getAppName();

    void setAppName(String appName);
}
