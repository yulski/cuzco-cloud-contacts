package com.yulski.cuzco;

import com.yulski.cuzco.controllers.ContactController;
import com.yulski.cuzco.controllers.DefaultController;
import com.yulski.cuzco.controllers.UserController;
import com.yulski.cuzco.util.*;
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

        // filters
        Filters filters = new Filters(flash);

        before(Paths.DASHBOARD, filters.loggedIn);
        before(Paths.PROFILE, filters.loggedIn);
        before(Paths.LOGIN, filters.notLoggedIn);
        before(Paths.LOGOUT, filters.loggedIn);
        before(Paths.EDIT_PROFILE, filters.loggedIn);
        before(Paths.DELETE_PROFILE, filters.loggedIn);
        before(Paths.REGISTRATION, filters.notLoggedIn);

        before(Paths.CONTACT, filters.loggedIn);
        before(Paths.USER_CONTACTS, filters.loggedIn);
        before(Paths.EDIT_CONTACT, filters.loggedIn);
        before(Paths.CREATE_CONTACT, filters.loggedIn);
        before(Paths.DELETE_CONTACT, filters.loggedIn);

        // default routes
        get(Paths.LANDING_PAGE, defaultController::getLandingPage);

        // User routes
        get(Paths.DASHBOARD, userController::getDashboard);
        get(Paths.PROFILE, userController::getOne);
        get(Paths.LOGIN, userController::getLoginForm);
        post(Paths.LOGIN, userController::login);
        get(Paths.LOGOUT, userController::logout);
        get(Paths.EDIT_PROFILE, userController::getEditForm);
        // TODO change to PUT
        post(Paths.EDIT_PROFILE, userController::edit);
        get(Paths.DELETE_PROFILE, userController::getDeleteForm);
        // TODO change to delete
        // TODO add a password confirmation page before the user is allowed to delete account. this page can be reused for different things, just redirect to different page after
        post(Paths.DELETE_PROFILE, userController::delete);
        get(Paths.REGISTRATION, userController::getCreateForm);
        post(Paths.REGISTRATION, userController::create);

        // Contact routes
        get(Paths.CONTACT, contactController::getOne);
        get(Paths.USER_CONTACTS, contactController::getAllForUser);
        get(Paths.EDIT_CONTACT, contactController::getEditForm);
        // TODO change to put
        post(Paths.EDIT_CONTACT, contactController::edit);
        get(Paths.CREATE_CONTACT, contactController::getCreateForm);
        post(Paths.CREATE_CONTACT, contactController::create);
        get(Paths.DELETE_CONTACT, contactController::getDeleteForm);
        // TODO change to delete
        post(Paths.DELETE_CONTACT, contactController::delete);
    }

}
