package com.cdd.user.web.web.listener;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

/**
 * @author yangfengshan
 * @create 2021-03-23 18:28
 **/
@HandlesTypes(UserWebServletInitializer.class)
@Deprecated
public class ServletInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> initializerSet, ServletContext ctx) throws ServletException {

    }
}
