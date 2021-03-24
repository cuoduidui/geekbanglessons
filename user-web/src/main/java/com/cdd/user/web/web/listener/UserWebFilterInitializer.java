package com.cdd.user.web.web.listener;

import com.cdd.geekbanglessons.web.mvc.initializer.AbstractMyWebMvcInitializer;
import com.cdd.user.web.web.filter.CharsetEncodingFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * @author yangfengshan
 * @create 2021-03-23 18:31
 **/
public class UserWebFilterInitializer extends AbstractMyWebMvcInitializer {
    public UserWebFilterInitializer() {
        super("UserWebFilterInitializer", 10);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //设置拦截器
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("CharsetEncodingFilter", CharsetEncodingFilter.class);
//        filterRegistration.addMappingForServletNames();
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR);
        filterRegistration.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
//        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("jolokia-agent", "org.jolokia.http.AgentServlet");
//        servletRegistration.addMapping("/jolokia/*");
        servletContext.addListener(DBConnectionInitializerListener.class);
        servletContext.addListener(ComponentContextMbeanListener.class);
    }
}
