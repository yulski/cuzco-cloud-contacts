package com.yulski.cuzco.controllers;

import com.yulski.cuzco.models.User;
import com.yulski.cuzco.util.FlashMessageManager;
import com.yulski.cuzco.util.Paths;
import com.yulski.cuzco.util.Renderer;
import com.yulski.cuzco.util.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public class DefaultController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class.getCanonicalName());

    public DefaultController(Renderer renderer, FlashMessageManager flash) {
        super(renderer, flash);
    }

    public String getLandingPage(Request request, Response response) {
        User user = request.session().attribute("user");
        if(user != null) {
            logger.info("Redirecting to dashboard");
            response.redirect(Paths.DASHBOARD);
            return "";
        }
        logger.info("Rendering landing page");
        return render(request, Templates.LANDING_PAGE, null);
    }

}
