package com.cdd.user.web.sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @author yangfengshan
 * @create 2021-03-02 9:45
 **/
public class DBConnectionPoolManager {
    private static LinkedList<DBConnectionManager> connectionPoolList;
    private static Integer maxSize = 10;
    private static DataSource dataSource;

    public static void releaseAllConnection() {
        if (connectionPoolList == null || connectionPoolList.size() == 0) return;
        for (DBConnectionManager manager : connectionPoolList) {
            manager.releaseConnection();
        }
    }

    public static void setMaxSize(Integer maxSize) {
        DBConnectionPoolManager.maxSize = maxSize;
    }

    public static void setDataSource(DataSource dataSource) {
        DBConnectionPoolManager.dataSource = dataSource;
    }

    public static synchronized DBConnectionManager getDBConnectionManager() throws SQLException {
        if (connectionPoolList == null || connectionPoolList.size() < 1) {
//            String databaseURL = "jdbc:derby:/db/user-platform;create=true";
            Connection connection = dataSource.getConnection();
            DBConnectionManager manager = new DBConnectionManager();
            manager.setConnection(connection);
            maxSize++;
            return manager;
        }
        return connectionPoolList.removeFirst();
    }

    public static synchronized void returnDBConnectionManager(DBConnectionManager manager) throws SQLException {
        if (maxSize > 10) {
            manager.releaseConnection();
            maxSize--;
            return;
        }
        if (connectionPoolList == null) {
            connectionPoolList = new LinkedList<>();
        }
        connectionPoolList.add(manager);
    }
}
