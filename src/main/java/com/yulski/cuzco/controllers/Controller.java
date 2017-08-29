package com.yulski.cuzco.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yulski.cuzco.util.Renderer;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    protected Gson gson;
    protected Renderer renderer;

    protected Controller(Renderer renderer) {
        this.renderer = renderer;
        gson = new Gson();
    }

    protected boolean acceptsJson(Request request) {
        return request.headers("Accept").contains("application/json");
    }

    protected boolean isJson(Request request) {
        return request.headers("Content-Type").contains("application/json");
    }


    protected String render(String templateName, Map<String, Object> model) {
        if(model == null) {
            model = new HashMap<>();
        }
        return renderer.render(templateName, model);
    }

    protected String getOutcomeJson(boolean success) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", "" + success);
        return gson.toJson(jsonObject);
    }

    protected Map<String, Object> getOutcomeMap(boolean success) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", "" + success);
        return map;
    }

}
