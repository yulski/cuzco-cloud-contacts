package com.yulski.cuzco.util;

import spark.Session;

public class FlashMessageManager {

    public static final String FLASH_MESSAGE = "flashMessage";
    public static final String FLASH_MESSAGE_TYPE = "flashMessageType";

    public String getFlashMessage(Session session) {
        String msg = session.attribute(FLASH_MESSAGE);
        session.removeAttribute(FLASH_MESSAGE);
        return msg;
    }

    public String getFlashMessageType(Session session) {
        String msg = session.attribute(FLASH_MESSAGE_TYPE);
        session.removeAttribute(FLASH_MESSAGE_TYPE);
        return msg;
    }

    public void setFlashMessage(String message, String type, Session session) {
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
