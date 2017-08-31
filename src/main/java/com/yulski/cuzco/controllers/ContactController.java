package com.yulski.cuzco.controllers;

import com.google.gson.JsonObject;
import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import com.yulski.cuzco.services.ContactService;
import com.yulski.cuzco.services.Service;
import com.yulski.cuzco.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yulski.cuzco.util.RequestUtil.acceptsJson;
import static com.yulski.cuzco.util.RequestUtil.isJson;

public class ContactController extends ModelController<Contact, ContactService> {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class.getCanonicalName());

    public ContactController(Renderer renderer, FlashMessageManager flash) {
        super(renderer, flash);
    }

    @Override
    public String getOne(Request request, Response response) {
        logger.info("Get one contact");
        int id = Integer.parseInt(request.params("id"));
        Contact contact = service.getOne(id);
        if(acceptsJson(request)) {
            logger.info("Returning contact as JSON");
            return gson.toJson(contact);
        } else {
            logger.info("Rendering contact page");
            Map<String, Object> model = new HashMap<>();
            model.put("contact", contact);
            return render(request, Templates.CONTACT, model);
        }
    }

    public String getAllForUser(Request request, Response response) {
        logger.info("Get all user contacts");
        User user = request.session().attribute("user");
        List<Contact> contacts = service.getContactsForUser(user);
        if(acceptsJson(request)) {
            logger.info("Returning list of user contacts as JSON");
            return gson.toJson(contacts);
        } else {
            logger.info("Rendering user's contact list");
            Map<String, Object> model = new HashMap<>();
            model.put("contacts", contacts);
            return render(request, Templates.USER_CONTACTS, model);
        }
    }

    @Override
    public String getEditForm(Request request, Response response) {
        logger.info("Get edit contact form");
        int id = Integer.parseInt(request.params("id"));
        Contact contact = service.getOne(id);
        logger.info("Rendering contact edit page");
        Map<String, Object> model = new HashMap<>();
        model.put("contact", contact);
        return render(request, Templates.EDIT_CONTACT, model);
    }

    @Override
    public String edit(Request request, Response response) {
        logger.info("Edit a contact");
        int id = Integer.parseInt(request.params("id"));
        Contact oldContact = service.getOne(id);
        Contact contact;
        if(isJson(request)) {
            logger.info("Processing JSON request to edit contact");
            contact = gson.fromJson(request.body(), Contact.class);
            contact.setId(id);
        } else {
            logger.info("Processing form to edit contact");
            contact = Contact.builder()
                    .id(id)
                    .name(request.queryParams("name"))
                    .phoneNumber(request.queryParams("phone-number"))
                    .user(oldContact.getUser())
                    .build();
        }
        boolean success = service.update(contact);
        if(success) {
            flash.setFlashMessage("Contact information updated successfully", "success", request.session());
        } else {
            flash.setFlashMessage("Failed to update contact information", "error", request.session());
        }
        if(acceptsJson(request)) {
            logger.info("Sending JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Redirecting to edited contact page");
            response.redirect(Paths.generatePath(Paths.CONTACT, Integer.toString(id)));
            return "";
        }
    }

    @Override
    public String getCreateForm(Request request, Response response) {
        logger.info("Get create contact form");
        User user = request.session().attribute("user");
        logger.info("Rendering create contact page");
        return render(request, Templates.CREATE_CONTACT, null);
    }

    @Override
    public String create(Request request, Response response) {
        logger.info("Create contact");
        Contact contact;
        if(isJson(request)) {
            logger.info("Processing JSON request to create contact");
            contact = gson.fromJson(request.body(), Contact.class);
        } else {
            logger.info("Processing submitted form to create contact");
            contact = Contact.builder()
                    .name(request.queryParams("name"))
                    .phoneNumber(request.queryParams("phoneNumber"))
                    .user(request.session().attribute("user"))
                    .build();
        }
        int createdId = service.create(contact);
        boolean success = createdId != Service.UPDATE_FAILURE;
        if(success) {
            flash.setFlashMessage("Created contact successfully", "success", request.session());
        } else {
            flash.setFlashMessage("Failed to create contact", "error", request.session());
        }
        if(acceptsJson(request)) {
            logger.info("Sending JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Redirecting to the newly created contact page");
            response.redirect(Paths.generatePath(Paths.CONTACT, Integer.toString(createdId)));
            return "";
        }
    }

    @Override
    public String getDeleteForm(Request request, Response response) {
        logger.info("Get delete contact form");
        int id = Integer.parseInt(request.params("id"));
        Contact contact = service.getOne(id);
        logger.info("Rendering delete contact form");
        Map<String, Object> model = new HashMap<>();
        model.put("contact", contact);
        return render(request, Templates.DELETE_CONTACT, model);
    }

    @Override
    public String delete(Request request, Response response) {
        logger.info("Delete contact");
        int id = Integer.parseInt(request.params("id"));
        boolean success = service.delete(id);
        if(success) {
            flash.setFlashMessage("Contact deleted successfully", "success", request.session());
        } else {
            flash.setFlashMessage("Failed to delete contact", "error", request.session());
        }
        if(acceptsJson(request)) {
            logger.info("Sending JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Redirecting to dashboard");
            response.redirect(Paths.DASHBOARD);
            return "";
        }
    }

    @Override
    protected ContactService getService() {
        return new ContactService();
    }
}
