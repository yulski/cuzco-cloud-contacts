package com.yulski.cuzco.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

import static com.yulski.cuzco.util.RequestUtil.acceptsJson;
import static spark.Spark.halt;

public class Filters {

    private static final Logger logger = LoggerFactory.getLogger(Filters.class.getCanonicalName());

    private FlashMessageManager flash;
    private Gson gson;

    public final Filter loggedIn = (Request request, Response response) -> {
        if(request.session().attribute("user") == null) {
            logger.error("Anon attempting to access page meant for logged in users");
            flash.setFlashMessage("You are not logged in", "error", request.session());
            if(acceptsJson(request)) {
                logger.info("Halting request and returning empty JSON");
                halt(400, gson.toJson(new JsonObject()));
            } else {
                logger.info("Redirecting to landing page");
                response.redirect(Paths.LANDING_PAGE);
                halt(400);
            }
        }
    };

    public final Filter notLoggedIn = (Request request, Response response) -> {
        if(request.session().attribute("user") != null) {
            logger.error("Logged in user attempting to access page meant for anons");
            flash.setFlashMessage("You are already logged in", "error", request.session());
            if(acceptsJson(request)) {
                logger.info("Halting request and returning empty JSON");
                halt(400, gson.toJson(new JsonObject()));
            } else {
                logger.info("Redirecting to dashboard");
                response.redirect(Paths.DASHBOARD);
                halt(400);
            }
        }
    };

    public Filters(FlashMessageManager flash) {
        this.flash = flash;
        this.gson = new Gson();
    }
}
