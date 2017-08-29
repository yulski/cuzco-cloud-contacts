package com.yulski.cuzco.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import com.yulski.cuzco.services.ContactService;
import com.yulski.cuzco.services.UserService;
import com.yulski.cuzco.util.Renderer;
import com.yulski.cuzco.util.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.template.jtwig.JtwigTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController extends ModelController<User, UserService> {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class.getCanonicalName());

    public UserController(Renderer renderer) {
        super(renderer);
    }

    public String getDashboard(Request request, Response response) {
        logger.info("Get user dashboard");
        User user = request.session().attribute("user");
        if(user == null) {
            logger.error("Attempting to view dashboard when no user logged in");
            response.status(400);
            logger.info("Rendering landing page");
            return render(Templates.LANDING_PAGE, null);
        }
        logger.info("Getting user contacts");
        List<Contact> contacts = new ContactService().getContactsForUser(user); // TODO fix this - this shouldn't be directly instantiating a ContactService
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("contacts", contacts);
        return render(Templates.DASHBOARD, model);
    }

    @Override
    public String getOne(Request request, Response response) {
        logger.info("Get user profile");
        User user = request.session().attribute("user");
        if(user == null) {
            logger.error("Attempting to view profile when no user logged in");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                final String template = request.session().attribute("user") == null ?
                        Templates.LANDING_PAGE : Templates.DASHBOARD;
                logger.info("rendering " + template);
                return render(template, null);
            }
        }
        if (acceptsJson(request)) {
            logger.info("Returning user as JSON");
            return gson.toJson(user);
        } else {
            logger.info("rendering user profile");
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            return render(Templates.USER_PROFILE, model);
        }
    }

    public String getLoginForm(Request request, Response response) {
        logger.info("Get login form");
        if(request.session().attribute("user") != null) {
            logger.error("User is already logged in.");
            response.status(400);
            logger.info("Rendering dashboard.");
            return render(Templates.DASHBOARD, null);
        }
        logger.info("Rendering login form");
        return render(Templates.LOGIN, null);
    }

    public String login(Request request, Response response) {
        logger.info("User trying to log in");
        if(request.session().attribute("user") != null) {
            logger.error("Logged in user attempting to log in.");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering dashboard");
                return render(Templates.DASHBOARD, null);
            }
        }
        String email = request.queryParams("email");
        String password = request.queryParams("password");
        if(!(email != null && email.length() > 0 && password != null && password.length() > 0)) {
            logger.error("Invalid vales submitted for login. (email='" +
                    email + "',password='" + password + "')");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering landing page");
                return render(Templates.LANDING_PAGE, null);
            }
        }
        logger.info("Attempting to authenticate user");
        boolean success = service.authenticate(email, password);
        final String template;
        if(success) {
            logger.info("User authentication successful. Storing user in session");
            template = Templates.DASHBOARD;
            request.session().attribute("user", service.getOneByEmail(email));
        } else {
            logger.info("User authentication failed");
            template = Templates.LANDING_PAGE;
        }
        if(acceptsJson(request)) {
            logger.info("Returning JSON");
            return getOutcomeJson(success);
        } else {
            logger.info("Rendering template " + template);
            return render(template, getOutcomeMap(success));
        }
    }

    public String logout(Request request, Response response) {
        logger.info("Log out user");
        if(request.session().attribute("user") == null) {
            logger.error("Logout attempt with no user logged in");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering landing page");
                return render(Templates.LANDING_PAGE, null);
            }
        }
        logger.info("Removing user from session");
        request.session().removeAttribute("user");
        if(acceptsJson(request)) {
            logger.info("Returning JSON response");
            return getOutcomeJson(true);
        } else {
            logger.info("Rendering landing page");
            return render(Templates.LANDING_PAGE, getOutcomeMap(true));
        }
    }

    @Override
    public String getEditForm(Request request, Response response) {
        logger.info("Getting edit profile page");
        if(request.session().attribute("user") == null) {
            logger.error("Request for edit profile form when no user logged in");
            response.status(400);
            logger.info("Rendering landing page");
            return render(Templates.LANDING_PAGE, null);
        }
        User user = request.session().attribute("user");
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        logger.info("Rendering form for editing user with id " + user.getId());
        return render(Templates.EDIT_USER, model);
    }

    @Override
    public String edit(Request request, Response response) {
        logger.info("Editing user profile");
        User loggedInUser = request.session().attribute("user");
        if(loggedInUser == null) {
            logger.error("Attempt to edit profile when no user logged in");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering landing page template");
                return render(Templates.LANDING_PAGE, null);
            }
        }
        User user = loggedInUser;
        String email;
        String password;
        boolean hasChanges = false;
        if(isJson(request)) {
            logger.info("Processing JSON request to edit user profile");
            JsonObject json = new JsonParser().parse(request.body()).getAsJsonObject();
            email = json.get("email").getAsString();
            password = json.get("password").getAsString();
        } else {
            email = request.queryParams("email");
            password = request.queryParams("password");
        }
        if(email == null && password == null) {
            logger.error("No values submitted in edit user request");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering dashboard");
                return render(Templates.DASHBOARD, null);
            }
        }
        if(!email.equals(loggedInUser.getEmail())) {
            hasChanges = true;
            user.setEmail(email);
        }
        if(!password.equals(loggedInUser.getPassword())) {
            hasChanges = true;
            user.setPassword(password);
        }
        if(!hasChanges) {
            logger.error("Submitted edit profile request with no changes");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering dashboard");
                return render(Templates.DASHBOARD, null);
            }
        }
        logger.info("Editing user's profile");
        boolean success = service.update(user);
        if(acceptsJson(request)) {
            logger.info("Returning JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Rendering user profile page");
            return render(Templates.USER_PROFILE, getOutcomeMap(success));
        }
    }

    @Override
    public String getCreateForm(Request request, Response response) {
        logger.info("Get registration form");
        if(request.session().attribute("user") != null) {
            logger.error("Logged in user attempting to go to registration page");
            response.status(400);
            logger.info("Rendering dashboard");
            return render(Templates.DASHBOARD, null);
        }
        logger.info("Rendering registration page");
        return render(Templates.REGISTRATION, null);
    }

    @Override
    public String create(Request request, Response response) {
        logger.info("Registration form submitted");
        if(request.session().attribute("user") != null) {
            logger.error("Logged in user attempting to register");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering dashboard");
                return render(Templates.DASHBOARD, null);
            }
        }
        User user;
        if(isJson(request)) {
            logger.info("Processing JSON request to create new user");
            user = gson.fromJson(request.body(), User.class);
        } else {
            logger.info("Processing submitted form to create new user");
            user = User.builder()
                    .email(request.queryParams("email"))
                    .password(request.queryParams("password"))
                    .build();
        }
        if(!user.isValid()) {
            logger.error("Invalid registration form submitted. User is not valid");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering landing page");
                return render(Templates.LANDING_PAGE, null);
            }
        }
        if(service.getOneByEmail(user.getEmail()) != null) {
            logger.error("Invalid registration form submitted. The email address is already in use");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering landing page");
                return render(Templates.LANDING_PAGE, null);
            }
        }
        logger.info("Creating new user");
        boolean success = service.create(user);
        if(success) {
            logger.info("User registration successful. Logging user in");
            request.session().attribute("user", user);
        }
        if(acceptsJson(request)) {
            logger.info("Returning JSON response");
            return getOutcomeJson(success);
        } else {
            final String template = success ? Templates.DASHBOARD : Templates.LANDING_PAGE;
            logger.info("Rendering template " + template);
            return render(template, getOutcomeMap(success));
        }
    }

    @Override
    public String delete(Request request, Response response) {
        logger.info("Request to delete user account");
        User user = request.session().attribute("user");
        if(user == null) {
            logger.error("Attempt to delete account when no user logged in");
            response.status(400);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering landing page");
                return render(Templates.LANDING_PAGE, null);
            }
        }
        logger.info("Deleting user account");
        boolean success = service.delete(user.getId());
        logger.info("Logging user out");
        request.session().removeAttribute("user");
        if(acceptsJson(request)) {
            logger.info("Returning JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Rendering landing page");
            return render(Templates.LANDING_PAGE, getOutcomeMap(success));
        }
    }

    protected UserService getService() {
        return new UserService();
    }
}
