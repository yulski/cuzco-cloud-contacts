package com.yulski.cuzco;

import com.yulski.cuzco.controllers.ContactController;
import com.yulski.cuzco.controllers.DefaultController;
import com.yulski.cuzco.controllers.UserController;
import com.yulski.cuzco.util.Env;
import com.yulski.cuzco.util.Paths;
import com.yulski.cuzco.util.Renderer;
import spark.template.jtwig.JtwigTemplateEngine;

import static spark.Spark.*;

public class Cuzco {

    private static final JtwigTemplateEngine jtwigEngine = new JtwigTemplateEngine("/templates");

    public static void main(String[] args) {
        Renderer renderer = new Renderer(jtwigEngine, Env.getTemplatesDir());

        DefaultController defaultController = new DefaultController(renderer);
        UserController userController = new UserController(renderer);
        ContactController contactController = new ContactController(renderer);

        port(Env.getPort());

        // default routes
        get(Paths.LANDING_PAGE, defaultController::getLandingPage);

        // User routes
        get(Paths.DASHBOARD, userController::getDashboard);
        get(Paths.PROFILE, userController::getOne);
        delete(Paths.PROFILE, userController::delete);
        get(Paths.LOGIN, userController::getLoginForm);
        post(Paths.LOGIN, userController::login);
        post(Paths.LOGOUT, userController::logout);
        get(Paths.EDIT_PROFILE, userController::getEditForm);
        put(Paths.EDIT_PROFILE, userController::edit);
        get(Paths.REGISTRATION, userController::getCreateForm);
        post(Paths.REGISTRATION, userController::create);

        // Contact routes
        get(Paths.CONTACT, contactController::getOne);
        delete(Paths.CONTACT, contactController::delete);
        get(Paths.USER_CONTACTS, contactController::getAllForUser);
        get(Paths.EDIT_CONTACT, contactController::getEditForm);
        put(Paths.EDIT_CONTACT, contactController::edit);
        get(Paths.CREATE_CONTACT, contactController::getCreateForm);
        post(Paths.CREATE_CONTACT, contactController::create);
    }

}
