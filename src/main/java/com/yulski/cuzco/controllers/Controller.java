package com.yulski.cuzco.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.ModelAndView;
import spark.Request;
import spark.template.jtwig.JtwigTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    protected JtwigTemplateEngine jtwigEngine;
    protected Gson gson;

    public Controller(JtwigTemplateEngine jtwigEngine) {
        this.jtwigEngine = jtwigEngine;
        gson = new Gson();
    }

    protected boolean acceptsJson(Request request) {
        return request.headers("Accept").contains("application/json");
    }

    protected boolean isJson(Request request) {
        return request.headers("Content-Type").contains("application/json");
    }

    protected String render(Map<String, Object> model, String templateName) {
        return jtwigEngine.render(new ModelAndView(model, templateName));
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
