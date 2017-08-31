package com.yulski.cuzco.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import com.yulski.cuzco.services.ContactService;
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

    public final Filter contactAccess = (Request request, Response response) -> {
        User user = request.session().attribute("user");
        int id = Integer.parseInt(request.params("id"));
        Contact contact = new ContactService().getOne(id);
        if(contact == null || contact.getUser().getId() != user.getId()) {
            logger.error("User trying to access contact they are not authorized to view");
            flash.setFlashMessage("You don't have a contact with id " + id, "error", request.session());
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
