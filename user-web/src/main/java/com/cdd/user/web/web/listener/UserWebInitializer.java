package com.cdd.user.web.web.listener;

import com.cdd.geekbanglessons.web.mvc.initializer.AbstractMyWebMvcInitializer;
import com.cdd.user.web.web.filter.CharsetEncodingFilter;

import javax.servlet.*;
import java.util.EnumSet;

/**
 * @author yangfengshan
 * @create 2021-03-23 18:31
 **/
public class UserWebInitializer extends AbstractMyWebMvcInitializer {
    public UserWebInitializer() {
        super("UserWebFilterInitializer", 10);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //设置拦截器
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("CharsetEncodingFilter", CharsetEncodingFilter.class);
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.ERROR);
        filterRegistration.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("jolokia-agent", "org.jolokia.http.AgentServlet");
        servletRegistration.addMapping("/jolokia/*");
//        servletContext.addListener(DBConnectionInitializerListener.class);
        servletContext.addListener(ComponentContextMbeanListener.class);
        servletContext.addListener(ConfigServletRequestListener.class);
    }
}
