package com.cdd.user.web.web.listener;

import com.cdd.user.web.sql.DBConnectionPoolManager;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class DBConnectionInitializerListener implements ServletContextListener {
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

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBConnectionPoolManager.releaseAllConnection();
    }
}
