package com.cdd.geekbanglessons.web.mvc.initializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author yangfengshan
 * @create 2021-03-23 16:09
 **/
public interface MyWebMvcInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
