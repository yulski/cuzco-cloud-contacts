package com.yulski.cuzco.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yulski.cuzco.util.Renderer;
import spark.Request;
import spark.Session;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    private static final String FLASH_MESSAGE = "flashMessage";
    private static final String FLASH_MESSAGE_TYPE = "flashMessageType";

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


    protected String render(Request request, String templateName, Map<String, Object> model) {
        if(model == null) {
            model = new HashMap<>();
        }
        model.put(FLASH_MESSAGE, getFlashMessage(request.session()));
        model.put(FLASH_MESSAGE_TYPE, getFlashMessageType(request.session()));
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

    private String getFlashMessage(Session session) {
        String msg = session.attribute(FLASH_MESSAGE);
        session.removeAttribute(FLASH_MESSAGE);
        return msg;
    }

    private String getFlashMessageType(Session session) {
        String msg = session.attribute(FLASH_MESSAGE_TYPE);
        session.removeAttribute(FLASH_MESSAGE_TYPE);
        return msg;
    }

    protected void setFlashMessage(String message, String type, Session session) {
        setFlashMessage(message, session);
        setFlashMessageType(type, session);
    }

    private void setFlashMessage(String msg, Session session) {
        session.attribute(FLASH_MESSAGE, msg);
    }

    private void setFlashMessageType(String type, Session session) {
        session.attribute(FLASH_MESSAGE_TYPE, type);
    }

}
