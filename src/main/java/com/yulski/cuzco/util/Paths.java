package com.yulski.cuzco.util;

import java.util.HashMap;
import java.util.Map;

public class Paths {
    // User paths
    public static final String LANDING_PAGE = "/";

    public static final String DASHBOARD = "/dashboard";
    public static final String PROFILE = "/profile";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String EDIT_PROFILE = "/user/edit";
    public static final String REGISTRATION = "/register";

    // Contact paths
    public static final String CONTACT = "/contact/:id";
    public static final String USER_CONTACTS = "/my-contacts";
    public static final String EDIT_CONTACT = "/edit-contact/:id";
    public static final String CREATE_CONTACT = "/create-contact";

    public static Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("LANDING_PAGE", LANDING_PAGE);
        map.put("DASHBOARD", DASHBOARD);
        map.put("PROFILE", PROFILE);
        map.put("LOGIN", LOGIN);
        map.put("LOGOUT", LOGOUT);
        map.put("EDIT_PROFILE", EDIT_PROFILE);
        map.put("REGISTRATION", REGISTRATION);
        map.put("CONTACT", CONTACT);
        map.put("USER_CONTACTS", USER_CONTACTS);
        map.put("EDIT_CONTACT", EDIT_CONTACT);
        map.put("CREATE_CONTACT", CREATE_CONTACT);
        return map;
    }
}
