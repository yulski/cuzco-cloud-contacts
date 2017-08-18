package com.yulski.cuzco;

import spark.ModelAndView;
import spark.template.jtwig.JtwigTemplateEngine;
import spark.Request;
import spark.Response;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.port;

public class Hello {

    private static final JtwigTemplateEngine jtwigEngine = new JtwigTemplateEngine("/templates");

    public static void main(String[] args) {
        port(getPortNumber());
        get("/", (Request request, Response response) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("projectName", "Cuzco");
            return jtwigEngine.render(new ModelAndView(model, "index.twig"));
        });
    }

    private static int getPortNumber() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

}
