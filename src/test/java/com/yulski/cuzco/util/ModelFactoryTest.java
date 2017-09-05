package com.yulski.cuzco.util;

import org.jtwig.JtwigModel;
import org.jtwig.reflection.model.Value;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ModelFactoryTest {

    @Test
    public void createModelWithEmptyMapContainsAllPathsTest() {
        ModelFactory modelFactory = new ModelFactory();
        JtwigModel model = modelFactory.createModel(new HashMap<>());

        checkPaths(model);
    }

    @Test
    public void createModelWithValuesContainsThoseValuesTest() {
        ModelFactory modelFactory = new ModelFactory();
        Map<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        map.put("foo", "bar");
        JtwigModel model = modelFactory.createModel(map);

        Value helloVal = model.get("hello").orNull();
        assertNotNull(helloVal);
        assertEquals("world", helloVal.getValue().toString());

        Value fooVal = model.get("foo").orNull();
        assertNotNull(fooVal);
        assertEquals("bar", fooVal.getValue().toString());
    }

    @Test
    public void createModelWithValuesAlsoContainsThoseValuesAndAllPathsTest() {
        ModelFactory modelFactory = new ModelFactory();
        Map<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        map.put("foo", "bar");
        JtwigModel model = modelFactory.createModel(map);

        Value helloVal = model.get("hello").orNull();
        assertNotNull(helloVal);
        assertEquals("world", helloVal.getValue().toString());

        Value fooVal = model.get("foo").orNull();
        assertNotNull(fooVal);
        assertEquals("bar", fooVal.getValue().toString());

        checkPaths(model);
    }

    private void checkPaths(JtwigModel model) {
        Map<String, Object> paths = Paths.getMap();
        for(String key : paths.keySet()) {
            Value val = model.get("PATH_" + key).orNull();
            assertNotNull(val);
            String modelPath = val.getValue().toString();
            assertEquals(paths.get(key), modelPath);
        }
    }

}
