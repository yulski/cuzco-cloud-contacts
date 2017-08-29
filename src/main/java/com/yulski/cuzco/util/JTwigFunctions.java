package com.yulski.cuzco.util;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import java.util.*;

public class JTwigFunctions {

    public static JtwigFunction path = new JtwigFunction() {
        @Override
        public String name() {
            return "path";
        }

        @Override
        public Collection<String> aliases() {
            Collection<String> aliases = new ArrayList<>();
            aliases.add("path");
            return aliases;
        }

        @Override
        public Object execute(FunctionRequest request) {
            List<Object> args = request.getArguments();
            String path = (String) args.get(0);
            if(args.size() == 1 || args.get(1) == null || ((Map<String, String>) args.get(1)).size() == 0) {
                return path;
            }
            Map<String, String> params = (Map<String, String>) args.get(1);
            return processParams(path, params);
        }

        private String processParams(String path, Map<String, String> params) {
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
    };

}
