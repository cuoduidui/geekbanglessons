package com.cdd.geekbanglessons.web.mvc.initializer;

import com.cdd.dependency.injection.context.ComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author yangfengshan
 * @create 2021-03-23 16:17
 **/
public class ComponentContextInitializer extends AbstractMyWebMvcInitializer {

    public ComponentContextInitializer() {
        super("com.cdd.geekbanglessons.web.mvc.initializer.ComponentContextInitializer", 10);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ComponentContext componentContext = ComponentContext.getInstance();
        componentContext.setClassLoader(servletContext.getClassLoader());
        componentContext.init();
        servletContext.setAttribute(ComponentContext.CONTEXT_NAME, componentContext);
    }
}
