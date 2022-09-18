package com.max.restaurant.model.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class MyDBConnection {
    private static Logger logger = LoggerFactory.getLogger(MyDBConnection.class);
    private static MyDBConnection myDbConnection;

    private MyDBConnection() throws SQLException, NamingException {
        getConnection();
    }

    public static MyDBConnection getInstance() throws SQLException, NamingException {
        logger.debug(CONSTRUCTOR);
        if (myDbConnection != null)
            return myDbConnection;

        synchronized (MyDBConnection.class) {
            myDbConnection = new MyDBConnection();
        }
        return myDbConnection;
    }

    public Connection getConnection() throws NamingException, SQLException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/restaurant");
        Connection conn = ds.getConnection();
        logger.debug(METHOD_STARTS_MSG, "getConnection", conn);
        return conn;
    }
}
