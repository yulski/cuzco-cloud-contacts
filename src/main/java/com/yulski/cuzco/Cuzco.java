package com.yulski.cuzco;

import com.yulski.cuzco.controllers.ContactController;
import com.yulski.cuzco.controllers.DefaultController;
import com.yulski.cuzco.controllers.UserController;
import com.yulski.cuzco.util.Env;
import com.yulski.cuzco.util.FlashMessageManager;
import com.yulski.cuzco.util.Paths;
import com.yulski.cuzco.util.Renderer;
import spark.template.jtwig.JtwigTemplateEngine;

import static spark.Spark.*;

public class Cuzco {

    public static void main(String[] args) {
        Renderer renderer = new Renderer(Env.getTemplatesDir());
        FlashMessageManager flash = new FlashMessageManager();

        DefaultController defaultController = new DefaultController(renderer, flash);
        UserController userController = new UserController(renderer, flash);
        ContactController contactController = new ContactController(renderer, flash);

        port(Env.getPort());

        // default routes
        get(Paths.LANDING_PAGE, defaultController::getLandingPage);

        // User routes
        get(Paths.DASHBOARD, userController::getDashboard);
        get(Paths.PROFILE, userController::getOne);
        // TODO change to delete
        // TODO add a password confirmation page before the user is allowed to delete account. this page can be reused for different things, just redirect to different page after
        post(Paths.PROFILE, userController::delete);
        get(Paths.LOGIN, userController::getLoginForm);
        post(Paths.LOGIN, userController::login);
        get(Paths.LOGOUT, userController::logout);
        get(Paths.EDIT_PROFILE, userController::getEditForm);
        // TODO change to PUT
        post(Paths.EDIT_PROFILE, userController::edit);
        get(Paths.REGISTRATION, userController::getCreateForm);
        post(Paths.REGISTRATION, userController::create);

        // Contact routes
        get(Paths.CONTACT, contactController::getOne);
        // TODO change to delete
        post(Paths.CONTACT, contactController::delete);
        get(Paths.USER_CONTACTS, contactController::getAllForUser);
        get(Paths.EDIT_CONTACT, contactController::getEditForm);
        // TODO change to put
        post(Paths.EDIT_CONTACT, contactController::edit);
        get(Paths.CREATE_CONTACT, contactController::getCreateForm);
        post(Paths.CREATE_CONTACT, contactController::create);
    }

}
