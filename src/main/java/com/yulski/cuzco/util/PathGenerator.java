package com.yulski.cuzco.util;

import java.util.Map;

public class PathGenerator {

    public String generatePath(String path, Map<String, String> params) {
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

    public String generatePath(String path, String... params) {
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
