package com.yulski.cuzco;

import com.yulski.cuzco.controllers.ContactController;
import com.yulski.cuzco.controllers.DefaultController;
import com.yulski.cuzco.controllers.UserController;
import com.yulski.cuzco.db.Db;
import com.yulski.cuzco.services.ContactService;
import com.yulski.cuzco.services.UserService;
import com.yulski.cuzco.util.*;

import static spark.Spark.*;

public class Cuzco {

    public static void main(String[] args) {
        Renderer renderer = new Renderer(Env.getTemplatesDir());
        SessionManager session = new SessionManager();

        Db db = new Db();

        UserService userService = new UserService(db);
        ContactService contactService = new ContactService(db, userService);

        DefaultController defaultController = new DefaultController(renderer, session);
        UserController userController = new UserController(renderer, session, userService, contactService);
        ContactController contactController = new ContactController(renderer, session, contactService);

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

        before(Paths.CONTACT, filters.loggedIn(Paths.LOGIN), filters.contactAccess(Paths.DASHBOARD, contactService));
        before(Paths.USER_CONTACTS, filters.loggedIn(Paths.LOGIN));
        before(Paths.EDIT_CONTACT, filters.loggedIn(Paths.LOGIN), filters.contactAccess(Paths.DASHBOARD, contactService));
        before(Paths.CREATE_CONTACT, filters.loggedIn(Paths.LOGIN));
        before(Paths.DELETE_CONTACT, filters.loggedIn(Paths.LOGIN), filters.contactAccess(Paths.DASHBOARD, contactService));

        // default routes
        get(Paths.LANDING_PAGE, defaultController::getLandingPage);

        // User routes
        get(Paths.DASHBOARD, userController::getDashboard);
        get(Paths.PROFILE, userController::getOne);
        get(Paths.LOGIN, userController::getLoginForm);
        post(Paths.LOGIN, userController::login);
        get(Paths.LOGOUT, userController::logout);
        get(Paths.EDIT_PROFILE, userController::getEditForm);
        post(Paths.EDIT_PROFILE, userController::edit);
        get(Paths.DELETE_PROFILE, userController::getDeleteForm);
        post(Paths.DELETE_PROFILE, userController::delete);
        get(Paths.REGISTRATION, userController::getCreateForm);
        post(Paths.REGISTRATION, userController::create);

        // Contact routes
        get(Paths.CONTACT, contactController::getOne);
        get(Paths.USER_CONTACTS, contactController::getAllForUser);
        get(Paths.EDIT_CONTACT, contactController::getEditForm);
        post(Paths.EDIT_CONTACT, contactController::edit);
        get(Paths.CREATE_CONTACT, contactController::getCreateForm);
        post(Paths.CREATE_CONTACT, contactController::create);
        get(Paths.DELETE_CONTACT, contactController::getDeleteForm);
        post(Paths.DELETE_CONTACT, contactController::delete);
    }

}
