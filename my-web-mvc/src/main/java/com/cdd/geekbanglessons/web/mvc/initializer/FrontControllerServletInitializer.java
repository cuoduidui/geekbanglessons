package com.cdd.geekbanglessons.web.mvc.initializer;

import com.cdd.geekbanglessons.web.mvc.FrontControllerServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author yangfengshan
 * @create 2021-03-23 17:49
 **/
public class FrontControllerServletInitializer extends AbstractMyWebMvcInitializer {
    public FrontControllerServletInitializer() {
        super("FrontControllerServletInitializer", 20);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ServletRegistration.Dynamic registration = servletContext.addServlet("FrontControllerServlet", FrontControllerServlet.class);
        registration.addMapping("/");
        registration.setLoadOnStartup(1);
        ServletRegistration.Dynamic defaultServlet = servletContext.addServlet("DefaultServlet", "org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addMapping("*.css");
        defaultServlet.addMapping("*.js");
        defaultServlet.setLoadOnStartup(1);
        defaultServlet.setInitParameter("debug", "0");
        defaultServlet.setInitParameter("listings", "false");
    }
}
