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

    private SessionManager session;
    private Gson gson;

    public Filters(SessionManager session) {
        this.session = session;
        this.gson = new Gson();
    }

    public Filter loggedIn(final String redirectPath) {
        return (Request request, Response response) -> {
            if(session.getUser(request.session()) == null) {
                logger.error("Anon attempting to access page meant for logged in users");
                session.setFlashMessage("You are not logged in", "error", request.session());
                handleFiltering(request, response, redirectPath);
            }
        };
    }

    public Filter notLoggedIn(final String redirectPath) {
        return (Request request, Response response) -> {
            if(session.getUser(request.session()) != null) {
                logger.error("Logged in user attempting to access page meant for anons");
                session.setFlashMessage("You are already logged in", "error", request.session());
                handleFiltering(request, response, redirectPath);
            }
        };
    }

    public Filter contactAccess(final String redirectPath, ContactService contactService) {
        return (Request request, Response response) -> {
            User user = session.getUser(request.session());
            int id = Integer.parseInt(request.params("id"));
            Contact contact = contactService.getOne(id);
            if(contact == null || contact.getUser().getId() != user.getId()) {
                logger.error("User trying to access contact they are not authorized to view");
                session.setFlashMessage("You don't have a contact with id " + id, "error", request.session());
                handleFiltering(request, response, redirectPath);
            }
        };
    }

    private void handleFiltering(Request request, Response response, String redirectPath) throws Exception {
        if(acceptsJson(request)) {
            logger.info("Halting request and returning empty JSON");
            throw halt(400, gson.toJson(new JsonObject()));
        } else {
            logger.info("Redirecting to " + redirectPath);
            response.redirect(redirectPath);
            throw halt(400);
        }
    }
}
