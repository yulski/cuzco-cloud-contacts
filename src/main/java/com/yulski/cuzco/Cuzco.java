package com.yulski.cuzco;

import com.yulski.cuzco.env.Env;
import spark.ModelAndView;
import spark.template.jtwig.JtwigTemplateEngine;
import spark.Request;
import spark.Response;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.port;

public class Cuzco {

    private static final JtwigTemplateEngine jtwigEngine = new JtwigTemplateEngine("/templates");

    public static void main(String[] args) {
        port(Env.getPort());
        get("/", (Request request, Response response) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("projectName", "Cuzco");
            return jtwigEngine.render(new ModelAndView(model, "index.twig"));
        });
    }

}
