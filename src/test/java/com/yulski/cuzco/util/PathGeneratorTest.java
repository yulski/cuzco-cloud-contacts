package com.yulski.cuzco.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PathGeneratorTest {

    @Test
    public void generatePathWithEmptyPathAndParamsShouldReturnEmptyStringTest() {
        PathGenerator pathGenerator = new PathGenerator();

        final String path = "";
        final Map<String, String> params = new HashMap<>();

        String result = pathGenerator.generatePath(path, params);
        assertEquals("", result);
    }

    @Test
    public void generatePathWithEmptyPathAndParamsVarargsShouldReturnEmptyStringTest() {
        PathGenerator pathGenerator = new PathGenerator();

        final String path = "";

        String result = pathGenerator.generatePath(path);
        assertEquals("", result);
    }

    @Test
    public void generatePathWithEmptyParamsShouldReturnOriginalPathTest() {
        PathGenerator pathGenerator = new PathGenerator();

        final String path = "/test/path";
        final Map<String, String> params = new HashMap<>();

        String result = pathGenerator.generatePath(path, params);
        assertEquals(path, result);
    }

    @Test
    public void generatePathWithEmptyParamsVarargsShouldReturnOriginalPathTest() {
        PathGenerator pathGenerator = new PathGenerator();

        final String path = "/test/path";

        String result = pathGenerator.generatePath(path);
        assertEquals(path, result);
    }

    @Test
    public void generatePathWithNoMatchingParamsShouldReturnOriginalPathTest() {
        PathGenerator pathGenerator = new PathGenerator();

        final String path = "/test/path/:id";
        final Map<String, String> params = new HashMap<>();
        params.put("first", "100");

        String result = pathGenerator.generatePath(path, params);
        assertEquals(path, result);
    }

    @Test
    public void generatePathWithMatchingParamsShouldGenerateCorrectPathTest() {
        PathGenerator pathGenerator = new PathGenerator();

        final String path = "/a/:first/:second";
        final Map<String, String> params = new HashMap<>();
        params.put("first", "test");
        params.put("second", "path");

        String result = pathGenerator.generatePath(path, params);
        assertEquals("/a/test/path", result);
    }

    @Test
    public void generatePathWithParamsVarargsShouldReturnCorrectPathTest() {
        PathGenerator pathGenerator = new PathGenerator();

        final String path = "/a/:first/:second";

        String result = pathGenerator.generatePath(path, "test", "path");
        assertEquals("/a/test/path", result);
    }

}
