package com.yulski.cuzco.controllers;

import com.yulski.cuzco.models.User;
import com.yulski.cuzco.util.Paths;
import com.yulski.cuzco.util.Renderer;
import com.yulski.cuzco.util.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public class DefaultController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class.getCanonicalName());

    public DefaultController(Renderer renderer) {
        super(renderer);
    }

    public String getLandingPage(Request request, Response response) {
        User user = request.session().attribute("user");
        if(user != null) {
            logger.info("Redirecting to dashboard");
            response.redirect(Paths.DASHBOARD);
            return "";
        }
        logger.info("Rendering landing page");
        return render(Templates.LANDING_PAGE, null);
    }

}
