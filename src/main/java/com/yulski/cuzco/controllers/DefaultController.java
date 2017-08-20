package com.yulski.cuzco.controllers;

import com.yulski.cuzco.util.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.jtwig.JtwigTemplateEngine;

import java.util.HashMap;

public class DefaultController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class.getCanonicalName());

    public DefaultController(JtwigTemplateEngine jtwigEngine) {
        super(jtwigEngine);
    }

    // TODO implement handler for landing page
    public String getLandingPage(Request request, Response response) {
        HashMap<String, Object> model = new HashMap<>();
        model.put("projectName", "Cuzco");
        return jtwigEngine.render(new ModelAndView(model, Templates.LANDING_PAGE));
    }

}
