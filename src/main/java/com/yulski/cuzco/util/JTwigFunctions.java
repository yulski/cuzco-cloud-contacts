package com.yulski.cuzco.util;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import java.util.*;
import java.util.*;

public class JTwigFunctions {

    private PathGenerator pathGenerator;

    public JTwigFunctions(PathGenerator pathGenerator) {
        this.pathGenerator = pathGenerator;
    }

    public JtwigFunction getPath() {
        return new JtwigFunction() {
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
                if (args.size() == 1 || args.get(1) == null || ((Map<String, String>) args.get(1)).size() == 0) {
                    return path;
                }
                Map<String, String> params = (Map<String, String>) args.get(1);
                return pathGenerator.generatePath(path, params);
            }
        };
    }

    public JtwigFunction getToString() {
        return new JtwigFunction() {
            @Override
            public String name() {
                return "toString";
            }

            @Override
            public Collection<String> aliases() {
                Collection<String> aliases = new ArrayList<>();
                aliases.add("toString");
                return aliases;
            }

            @Override
            public Object execute(FunctionRequest request) {
                Object obj = request.getArguments().get(0);
                return obj.toString();
            }
        };
    }

}
