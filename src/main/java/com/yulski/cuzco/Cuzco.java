package com.yulski.cuzco;

import com.yulski.cuzco.controllers.ContactController;
import com.yulski.cuzco.controllers.DefaultController;
import com.yulski.cuzco.controllers.UserController;
import com.yulski.cuzco.util.*;

import static spark.Spark.*;

public class Cuzco {

    public static void main(String[] args) {
        Renderer renderer = new Renderer(Env.getTemplatesDir());
        SessionManager session = new SessionManager();

        DefaultController defaultController = new DefaultController(renderer, session);
        UserController userController = new UserController(renderer, session);
        ContactController contactController = new ContactController(renderer, session);

        port(Env.getPort());

        // filters
        Filters filters = new Filters(session);

        before(Paths.DASHBOARD, filters.loggedIn(Paths.LANDING_PAGE));
        before(Paths.PROFILE, filters.loggedIn(Paths.LOGIN));
        before(Paths.LOGIN, filters.notLoggedIn(Paths.DASHBOARD));
        before(Paths.LOGOUT, filters.loggedIn(Paths.LOGIN));
        before(Paths.EDIT_PROFILE, filters.loggedIn(Paths.LOGIN));
        before(Paths.DELETE_PROFILE, filters.loggedIn(Paths.LOGIN));
        before(Paths.REGISTRATION, filters.notLoggedIn(Paths.DASHBOARD));

        before(Paths.CONTACT, filters.loggedIn(Paths.LOGIN), filters.contactAccess(Paths.DASHBOARD));
        before(Paths.USER_CONTACTS, filters.loggedIn(Paths.LOGIN));
        before(Paths.EDIT_CONTACT, filters.loggedIn(Paths.LOGIN), filters.contactAccess(Paths.DASHBOARD));
        before(Paths.CREATE_CONTACT, filters.loggedIn(Paths.LOGIN));
        before(Paths.DELETE_CONTACT, filters.loggedIn(Paths.LOGIN), filters.contactAccess(Paths.DASHBOARD));

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
