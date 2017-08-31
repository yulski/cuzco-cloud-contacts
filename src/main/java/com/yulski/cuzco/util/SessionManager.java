package com.yulski.cuzco.util;

import com.yulski.cuzco.models.User;
import spark.Session;

public class SessionManager {

    public static final String FLASH_MESSAGE = "flashMessage";
    public static final String FLASH_MESSAGE_TYPE = "flashMessageType";
    public static final String USER = "user";

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
        session.attribute(FLASH_MESSAGE, message);
        session.attribute(FLASH_MESSAGE_TYPE, type);
    }

    public User getUser(Session session) {
        return session.attribute(USER);
    }

    public void setUser(User user, Session session) {
        session.attribute(USER, user);
    }

    public void removeUser(Session session) {
        session.removeAttribute(USER);
    }

}
