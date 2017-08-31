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
    public static final String EDIT_PROFILE = "/edit-profile";
    public static final String DELETE_PROFILE = "/delete-profile";
    public static final String REGISTRATION = "/register";

    // Contact paths
    public static final String CONTACT = "/contact/:id";
    public static final String USER_CONTACTS = "/my-contacts";
    public static final String EDIT_CONTACT = "/edit-contact/:id";
    public static final String CREATE_CONTACT = "/create-contact";
    public static final String DELETE_CONTACT = "/delete-contact/:id";

    public static Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("LANDING_PAGE", LANDING_PAGE);
        map.put("DASHBOARD", DASHBOARD);
        map.put("PROFILE", PROFILE);
        map.put("LOGIN", LOGIN);
        map.put("LOGOUT", LOGOUT);
        map.put("EDIT_PROFILE", EDIT_PROFILE);
        map.put("DELETE_PROFILE", DELETE_PROFILE);
        map.put("REGISTRATION", REGISTRATION);
        map.put("CONTACT", CONTACT);
        map.put("USER_CONTACTS", USER_CONTACTS);
        map.put("EDIT_CONTACT", EDIT_CONTACT);
        map.put("CREATE_CONTACT", CREATE_CONTACT);
        map.put("DELETE_CONTACT", DELETE_CONTACT);
        return map;
    }

    public static String generatePath(String path, Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        String[] elems = path.split("/");
        for(int i=0; i<elems.length; i++) {
            if(elems[i].trim().length() == 0) {
                continue;
            }
            result.append("/");
            if(elems[i].startsWith(":") && params.containsKey(elems[i].substring(1))) {
                result.append(params.get(elems[i].substring(1)));
            } else {
                result.append(elems[i]);
            }
        }
        return result.toString();
    }

    public static String generatePath(String path, String... params) {
        StringBuilder result = new StringBuilder();
        String[] elems = path.split("/");
        int paramIndex = 0;
        for(int i=0; i<elems.length; i++) {
            if(elems[i].trim().length() == 0) {
                continue;
            }
            result.append("/");
            if(elems[i].startsWith(":")) {
                result.append(params[paramIndex]);
                paramIndex++;
            } else {
                result.append(elems[i]);
            }
        }
        return result.toString();
    }
}
