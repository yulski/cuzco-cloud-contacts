package com.yulski.cuzco.util;

import spark.Request;

public class RequestUtil {

    public static boolean acceptsJson(Request request) {
        String acceptHeader = request.headers("Accept");
        return acceptHeader != null && acceptHeader.contains("application/json");
    }

    public static boolean isJson(Request request) {
        String contentTypeHeader = request.headers("Content-Type");
        return contentTypeHeader != null && contentTypeHeader.contains("application/json");
    }

}
