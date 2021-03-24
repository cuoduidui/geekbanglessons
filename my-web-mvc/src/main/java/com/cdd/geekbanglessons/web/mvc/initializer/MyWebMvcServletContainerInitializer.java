package com.cdd.geekbanglessons.web.mvc.initializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author yangfengshan
 * @create 2021-03-23 16:28
 **/
@HandlesTypes(MyWebMvcInitializer.class)
public class MyWebMvcServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> initializerSet, ServletContext ctx) throws ServletException {
        if (null == initializerSet || initializerSet.isEmpty()) return;
        List<AbstractMyWebMvcInitializer> myWebMvcInitializerList = new LinkedList<>();
        for (Class<?> initializer : initializerSet) {
            if (!initializer.isInterface() && !Modifier.isAbstract(initializer.getModifiers()) &&
                    MyWebMvcInitializer.class.isAssignableFrom(initializer)) {
                try {
                    myWebMvcInitializerList.add((AbstractMyWebMvcInitializer) initializer.newInstance());
                } catch (Exception ex) {
                    throw new ServletException("com.cdd.geekbanglessons.web.mvc.initializer.MyWebMvcServletContainerInitializer 初始化一次", ex);
                }
            }
        }
        //排序
        myWebMvcInitializerList.sort(IntializerOrdinalComparator.INSTANCE);
        for (MyWebMvcInitializer myWebMvcInitializer : myWebMvcInitializerList) {
            try {
                myWebMvcInitializer.onStartup(ctx);
            } catch (Exception ex) {
                throw new ServletException(myWebMvcInitializer.getClass().getName(), ex);
            }

        }
    }
}
