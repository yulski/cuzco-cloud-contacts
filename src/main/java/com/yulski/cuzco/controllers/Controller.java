package com.yulski.cuzco.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yulski.cuzco.util.SessionManager;
import com.yulski.cuzco.util.Renderer;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    protected Gson gson;
    protected Renderer renderer;
    protected SessionManager session;

    protected Controller(Renderer renderer, SessionManager session) {
        this.renderer = renderer;
        this.session = session;
        gson = new Gson();
    }

    protected String render(Request request, String templateName, Map<String, Object> model) {
        if(model == null) {
            model = new HashMap<>();
        }
        model.put(SessionManager.FLASH_MESSAGE, session.getFlashMessage(request.session()));
        model.put(SessionManager.FLASH_MESSAGE_TYPE, session.getFlashMessageType(request.session()));
        model.put(SessionManager.USER, session.getUser(request.session()));
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
