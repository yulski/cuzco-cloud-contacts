package com.yulski.cuzco.db;

import lombok.Data;

@Data
public class DbConfig {

    private String host;
    private int port;
    private String name;
    private String schema;
    private String user;
    private String pass;

    public DbConfig(String host, int port, String name, String schema, String user, String pass) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.schema = schema;
        this.user = user;
        this.pass = pass;
    }
}
