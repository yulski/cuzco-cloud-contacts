package com.yulski.cuzco.util;

import java.util.HashMap;
import java.util.Map;

public class Env {

    // app config
    static final String PORT = "PORT";

    // database config
    static final String DB_HOST = "DB_HOST";
    static final String DB_PORT = "DB_PORT";
    static final String DB_NAME = "DB_NAME";
    static final String DB_SCHEMA = "DB_SCHEMA";
    static final String DB_USER = "DB_USER";
    static final String DB_PASS = "DB_PASS";
    static final String TEMPLATES_DIR = "TEMPLATES_DIR";

    private static final Map<String, String> CONFIG_VARS = new HashMap<>();

    static {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> environment = processBuilder.environment();

        // app
        CONFIG_VARS.put(PORT, environment.get(PORT));

        // database
        CONFIG_VARS.put(DB_HOST, environment.get(DB_HOST));
        CONFIG_VARS.put(DB_PORT, environment.get(DB_PORT));
        CONFIG_VARS.put(DB_NAME, environment.get(DB_NAME));
        CONFIG_VARS.put(DB_SCHEMA, environment.get(DB_SCHEMA));
        CONFIG_VARS.put(DB_USER, environment.get(DB_USER));
        CONFIG_VARS.put(DB_PASS, environment.get(DB_PASS));

        // templates
        CONFIG_VARS.put(TEMPLATES_DIR, environment.get(TEMPLATES_DIR));
    }

    public static int getPort() {
        return Integer.parseInt(CONFIG_VARS.get(PORT));
    }

    public static String getDbHost() {
        return CONFIG_VARS.get(DB_HOST);
    }

    public static int getDbPort() {
        return Integer.parseInt(CONFIG_VARS.get(DB_PORT));
    }

    public static String getDbName() {
        return CONFIG_VARS.get(DB_NAME);
    }

    public static String getDbSchema() {
        return CONFIG_VARS.get(DB_SCHEMA);
    }

    public static String getDbUser() {
        return CONFIG_VARS.get(DB_USER);
    }

    public static String getDbPass() {
        return CONFIG_VARS.get(DB_PASS);
    }

    public static String getTemplatesDir() {
        return CONFIG_VARS.get(TEMPLATES_DIR);
    }

}
