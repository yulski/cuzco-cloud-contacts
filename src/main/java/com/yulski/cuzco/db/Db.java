package com.yulski.cuzco.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Db {

    private static final Logger logger = LoggerFactory.getLogger(Db.class.getCanonicalName());

    private Connection connection;

    private DbConfig config;

    public Db(DbConfig config) {
        this.config = config;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    String.format("jdbc:postgresql://%s:%d/%s",
                            config.getHost(),
                            config.getPort(),
                            config.getName()
                    ),
                    config.getUser(),
                    config.getPass());
            logger.info("Db connected to database successfully");
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public DbConfig getConfig() {
        return config;
    }
}
