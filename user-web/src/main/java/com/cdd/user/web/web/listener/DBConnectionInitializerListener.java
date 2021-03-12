package com.cdd.user.web.web.listener;

import com.cdd.user.web.sql.DBConnectionPoolManager;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

public class DBConnectionInitializerListener implements ServletContextListener {
    /**
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化。
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/UserPlatformDB");
            if (dataSource != null) {
                DBConnectionPoolManager.setDataSource(dataSource);
                DBConnectionPoolManager.setMaxSize(10);
            } else {
                String databaseURL = "create=true";
                EmbeddedDataSource embeddedDataSource = new EmbeddedDataSource();
                embeddedDataSource.setDatabaseName("/db/user-platform");
                embeddedDataSource.setConnectionAttributes(databaseURL);
                DBConnectionPoolManager.setDataSource(embeddedDataSource);
                DBConnectionPoolManager.setMaxSize(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sce);
    }

    /**
     * 当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBConnectionPoolManager.releaseAllConnection();
        System.out.println(sce);
    }
}
