package com.yulski.cuzco.util;

import spark.Request;

public class RequestUtil {

    public static boolean acceptsJson(Request request) {
        return request.headers("Accept").contains("application/json");
    }

    public static boolean isJson(Request request) {
        return request.headers("Content-Type").contains("application/json");
    }

}
