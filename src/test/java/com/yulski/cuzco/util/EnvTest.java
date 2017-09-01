package com.yulski.cuzco.util;

import org.junit.*;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static org.junit.Assert.assertEquals;

public class EnvTest {

    @ClassRule
    public static final EnvironmentVariables envVars = new EnvironmentVariables();

    @BeforeClass
    public static void setupEnv() {
        envVars.set(Env.PORT, Integer.toString(port));
        envVars.set(Env.DB_PORT, Integer.toString(dbPort));
        envVars.set(Env.DB_HOST, dbHost);
        envVars.set(Env.DB_NAME, dbName);
        envVars.set(Env.DB_SCHEMA, dbSchema);
        envVars.set(Env.DB_USER, dbUser);
        envVars.set(Env.DB_PASS, dbPass);
        envVars.set(Env.TEMPLATES_DIR, templatesDir);
    }

    private static final int port = 9999;
    private static final int dbPort = 1234;
    private static final String dbHost = "localhost";
    private static final String dbName = "cuzco-db";
    private static final String dbSchema = "cuzco-schema";
    private static final String dbUser = "yulski";
    private static final String dbPass = "password_123";
    private static final String templatesDir = System.getProperty("user.dir");

    @Test
    public void getPortTest() {
        int envPort = Env.getPort();
        assertEquals(port, envPort);
    }

    @Test
    public void getDbPortTest() {
        int envDbPort = Env.getDbPort();
        assertEquals(dbPort, envDbPort);
    }

    @Test
    public void getDbHostTest() {
        String envDbHost = Env.getDbHost();
        assertEquals(dbHost, envDbHost);
    }

    @Test
    public void getDbNameTest() {
        String envDbName = Env.getDbName();
        assertEquals(dbName, envDbName);
    }

    @Test
    public void getDbSchemaTest() {
        String envDbSchema = Env.getDbSchema();
        assertEquals(dbSchema, envDbSchema);
    }

    @Test
    public void getDbUserTest() {
        String envDbUser = Env.getDbUser();
        assertEquals(dbUser, envDbUser);
    }

    @Test
    public void getDbPassTest() {
        String envDbPass = Env.getDbPass();
        assertEquals(dbPass, envDbPass);
    }

    @Test
    public void getTemplatesDirTest() {
        String envTemplatesDir = Env.getTemplatesDir();
        assertEquals(templatesDir, envTemplatesDir);
    }

}
