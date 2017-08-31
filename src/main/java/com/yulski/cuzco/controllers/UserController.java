package com.yulski.cuzco.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import com.yulski.cuzco.services.ContactService;
import com.yulski.cuzco.services.Service;
import com.yulski.cuzco.services.UserService;
import com.yulski.cuzco.util.FlashMessageManager;
import com.yulski.cuzco.util.Paths;
import com.yulski.cuzco.util.Renderer;
import com.yulski.cuzco.util.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yulski.cuzco.util.RequestUtil.acceptsJson;
import static com.yulski.cuzco.util.RequestUtil.isJson;

public class UserController extends ModelController<User, UserService> {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class.getCanonicalName());

    public UserController(Renderer renderer, FlashMessageManager flash) {
        super(renderer, flash);
    }

    public String getDashboard(Request request, Response response) {
        logger.info("Get user dashboard");
        User user = request.session().attribute("user");
        logger.info("Getting user contacts");
        List<Contact> contacts = new ContactService().getContactsForUser(user); // TODO fix this - this shouldn't be directly instantiating a ContactService
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("contacts", contacts);
        return render(request, Templates.DASHBOARD, model);
    }

    @Override
    public String getOne(Request request, Response response) {
        logger.info("Get user profile");
        User user = request.session().attribute("user");
        if (acceptsJson(request)) {
            logger.info("Returning user as JSON");
            return gson.toJson(user);
        } else {
            logger.info("rendering user profile");
            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            return render(request, Templates.USER_PROFILE, model);
        }
    }

    public String getLoginForm(Request request, Response response) {
        logger.info("Get login form");
        logger.info("Rendering login form");
        return render(request, Templates.LOGIN, null);
    }

    public String login(Request request, Response response) {
        logger.info("User trying to log in");
        String email = request.queryParams("email");
        String password = request.queryParams("password");
        if(email == null || email.trim().length() == 0 || password == null || password.trim().length() == 0) {
            logger.error("Invalid vales submitted for login. (email='" +
                    email + "',password='" + password + "')");
            flash.setFlashMessage("Invalid login request submitted", "error", request.session());
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Redirecting to landing page");
                response.redirect(Paths.LANDING_PAGE);
                return "";
            }
        }
        logger.info("Attempting to authenticate user");
        boolean success = service.authenticate(email, password);
        String redirectPath;
        if(success) {
            logger.info("User authentication successful. Storing user in session");
            flash.setFlashMessage("Logged in successfully", "success", request.session());
            request.session().attribute("user", service.getOneByEmail(email));
            redirectPath = Paths.DASHBOARD;
        } else {
            logger.info("User authentication failed");
            flash.setFlashMessage("Authentication failed", "error", request.session());
            redirectPath = Paths.LOGIN;
        }
        if(acceptsJson(request)) {
            logger.info("Returning JSON");
            return getOutcomeJson(success);
        } else {
            logger.info("Redirecting user to " + redirectPath);
            response.redirect(redirectPath);
            return "";
        }
    }

    public String logout(Request request, Response response) {
        logger.info("Log out user");
        logger.info("Removing user from session");
        request.session().removeAttribute("user");
        flash.setFlashMessage("Logged out successfully", "success", request.session());
        if(acceptsJson(request)) {
            logger.info("Returning JSON response");
            return getOutcomeJson(true);
        } else {
            logger.info("Redirecting to landing page");
            response.redirect(Paths.LANDING_PAGE);
            return "";
        }
    }

    @Override
    public String getEditForm(Request request, Response response) {
        logger.info("Getting edit profile page");
        User user = request.session().attribute("user");
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        logger.info("Rendering form for editing user with id " + user.getId());
        return render(request, Templates.EDIT_USER, model);
    }

    @Override
    public String edit(Request request, Response response) {
        logger.info("Editing user profile");
        User loggedInUser = request.session().attribute("user");
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
            flash.setFlashMessage("No values submitted", "error", request.session());
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Redirecting to dashboard");
                response.redirect(Paths.DASHBOARD);
                return "";
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
            flash.setFlashMessage("Submitted form with no changes", "error", request.session());
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Redirecting to dashboard");
                response.redirect(Paths.DASHBOARD);
                return "";
            }
        }
        logger.info("Editing user's profile");
        boolean success = service.update(user);
        if(success) {
            flash.setFlashMessage("Profile updated successfully", "success", request.session());
        } else {
            flash.setFlashMessage("Failed to update profile", "error", request.session());
        }
        if(acceptsJson(request)) {
            logger.info("Returning JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Rendering user's profile");
            response.redirect(Paths.PROFILE);
            return "";
        }
    }

    @Override
    public String getCreateForm(Request request, Response response) {
        logger.info("Get registration form");
        logger.info("Rendering registration page");
        return render(request, Templates.REGISTRATION, null);
    }

    @Override
    public String create(Request request, Response response) {
        logger.info("Registration form submitted");
        if(!request.queryParams("password1").equals(request.queryParams("password2"))) {
            logger.error("Passwords aren't equal");
            flash.setFlashMessage("Passwords don't match", "error", request.session());
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Redirecting to registration page");
                response.redirect(Paths.REGISTRATION);
                return "";
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
                    .password(request.queryParams("password1"))
                    .build();
        }
        if(!user.isValid()) {
            logger.error("Invalid registration form submitted. User is not valid");
            flash.setFlashMessage("Invalid form submitted", "error", request.session());
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Redirecting to registration page");
                response.redirect(Paths.REGISTRATION);
                return "";
            }
        }
        if(service.getOneByEmail(user.getEmail()) != null) {
            logger.error("Invalid registration form submitted. The email address is already in use");
            flash.setFlashMessage("Email address already in use", "error", request.session());
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Redirecting to registration page");
                response.redirect(Paths.REGISTRATION);
                return "";
            }
        }
        logger.info("Creating new user");
        int createdId = service.create(user);
        boolean success = createdId != Service.UPDATE_FAILURE;
        if(success) {
            logger.info("User registration successful. Logging user in");
            request.session().attribute("user", user);
            flash.setFlashMessage("Registered and logged in successfully", "success", request.session());
        } else {
            flash.setFlashMessage("Failed to register", "error", request.session());
        }
        if(acceptsJson(request)) {
            logger.info("Returning JSON response");
            return getOutcomeJson(success);
        } else {
            String redirectPath = success ? Paths.DASHBOARD : Paths.LANDING_PAGE;
            logger.info("Redirecting to " + redirectPath);
            response.redirect(redirectPath);
            return "";
        }
    }

    @Override
    public String getDeleteForm(Request request, Response response) {
        logger.info("Get delete account form");
        User user = request.session().attribute("user");
        logger.info("Rendering delete user form");
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        return render(request, Templates.DELETE_USER, model);
    }

    @Override
    public String delete(Request request, Response response) {
        logger.info("Request to delete user account");
        User user = request.session().attribute("user");
        logger.info("Authenticating request");
        if(!service.authenticate(user.getEmail(), request.queryParams("password"))) {
            logger.error("Incorrect password");
            flash.setFlashMessage("The password you entered is incorrect", "error", request.session());
            logger.info("Redirecting to delete user page");
            response.redirect(Paths.DELETE_PROFILE);
            return "";
        }
        logger.info("Deleting user account");
        boolean success = service.delete(user.getId());
        if(success) {
            logger.info("Logging user out");
            request.session().removeAttribute("user");
            flash.setFlashMessage("Your account was deleted successfully", "success", request.session());
        } else {
            flash.setFlashMessage("Failed to delete account", "error", request.session());
        }
        if(acceptsJson(request)) {
            logger.info("Returning JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Redirecting to landing page");
            response.redirect(Paths.LANDING_PAGE);
            return "";
        }
    }

    @Override
    protected UserService getService() {
        return new UserService();
    }
}
