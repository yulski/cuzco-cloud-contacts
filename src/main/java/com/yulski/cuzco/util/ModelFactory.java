package com.yulski.cuzco.util;

import org.jtwig.JtwigModel;

import java.util.HashMap;
import java.util.Map;

public class ModelFactory {

    private Map<String, Object> baseModel = new HashMap<>();

    public ModelFactory() {
        initBaseModel();
    }

    private void initBaseModel() {
        Map<String, Object> paths = Paths.getMap();
        for(String key: paths.keySet()) {
            baseModel.put("PATH_" + key, paths.get(key));
        }
    }

    JtwigModel createModel(Map<String, Object> map) {
        map.putAll(baseModel);
        JtwigModel model = JtwigModel.newModel();
        for(String key : map.keySet()) {
            model.with(key, map.get(key));
        }
        return model;
    }

}
