package com.yulski.cuzco.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yulski.cuzco.util.FlashMessageManager;
import com.yulski.cuzco.util.Renderer;
import spark.Request;
import spark.Session;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {

    protected Gson gson;
    protected Renderer renderer;
    protected FlashMessageManager flash;

    protected Controller(Renderer renderer, FlashMessageManager flash) {
        this.renderer = renderer;
        this.flash = flash;
        gson = new Gson();
    }

    protected String render(Request request, String templateName, Map<String, Object> model) {
        if(model == null) {
            model = new HashMap<>();
        }
        model.put(FlashMessageManager.FLASH_MESSAGE, flash.getFlashMessage(request.session()));
        model.put(FlashMessageManager.FLASH_MESSAGE_TYPE, flash.getFlashMessageType(request.session()));
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
