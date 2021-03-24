package com.cdd.user.web.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author yangfengshan
 * @create 2021-03-23 18:27
 **/
@Deprecated
public interface UserWebServletInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
