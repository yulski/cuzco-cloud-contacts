package com.yulski.cuzco.db;

import com.yulski.cuzco.env.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Db {

    private static final Logger logger = LoggerFactory.getLogger(Db.class.getCanonicalName());

    private Connection connection;

    public Db() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    String.format("jdbc:postgresql://%s:%d/%s",
                            Env.getDbHost(),
                            Env.getDbPort(),
                            Env.getDbName()
                    ),
                    Env.getDbUser(),
                    Env.getDbPass());
            logger.info("Db connected to database successfully");
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
