package com.cdd.user.web.web.listener;

import com.cdd.user.web.web.Mbean.WebContext;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;

/**
 * @author yangfengshan
 * @create 2021-03-15 18:45
 **/
public class ComponentContextMbeanListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // 获取平台 MBean Server
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            // 为 UserMXBean 定义 ObjectName
            ObjectName objectName = null;
            objectName = new ObjectName("com.cdd.user.web.web.Mbean:type=WebContext");
            mBeanServer.registerMBean(new WebContext(), objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
