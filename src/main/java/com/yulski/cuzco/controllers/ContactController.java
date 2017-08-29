package com.yulski.cuzco.controllers;

import com.google.gson.JsonObject;
import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import com.yulski.cuzco.services.ContactService;
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

public class ContactController extends ModelController<Contact, ContactService> {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class.getCanonicalName());

    public ContactController(Renderer renderer) {
        super(renderer);
    }

    @Override
    public String getOne(Request request, Response response) {
        logger.info("Get one contact");
        int id = Integer.parseInt(request.params("id"));
        Contact contact = service.getOne(id);
        if(contact == null) {
            logger.error("No contact found for id " + id);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering dashboard");
                return render(Templates.DASHBOARD, null);
            }
        }
        if(acceptsJson(request)) {
            logger.info("Returning contact as JSON");
            return gson.toJson(contact);
        } else {
            logger.info("Rendering contact page");
            Map<String, Object> model = new HashMap<>();
            model.put("contact", contact);
            return render(Templates.CONTACT, model);
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
            return render(Templates.DASHBOARD, model);
        }
    }

    @Override
    public String getEditForm(Request request, Response response) {
        logger.info("Get edit contact form");
        int id = Integer.parseInt(request.params("id"));
        Contact contact = service.getOne(id);
        if(contact == null) {
            logger.error("No contact found for id " + id);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering dashboard");
                return render(Templates.DASHBOARD, null);
            }
        }
        logger.info("Rendering contact edit page");
        Map<String, Object> model = new HashMap<>();
        model.put("contact", contact);
        return render(Templates.EDIT_CONTACT, model);
    }

    @Override
    public String edit(Request request, Response response) {
        logger.info("Edit a contact");
        int id = Integer.parseInt(request.params("id"));
        Contact oldContact = service.getOne(id);
        if(oldContact == null) {
            logger.error("No contact found for id " + id);
            if(acceptsJson(request)) {
                logger.info("Returning empty JSON");
                return gson.toJson(new JsonObject());
            } else {
                logger.info("Rendering dashboard");
                return render(Templates.DASHBOARD, null);
            }
        }
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
        if(acceptsJson(request)) {
            logger.info("Sending JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Rendering edited contact page");
            return render(Templates.CONTACT, getOutcomeMap(success));
        }
    }

    @Override
    public String getCreateForm(Request request, Response response) {
        logger.info("Get create contact form");
        User user = request.session().attribute("user");
        logger.info("Rendering create contact page");
        return render(Templates.CREATE_CONTACT, null);
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
                    .phoneNumber(request.queryParams("phone-number"))
                    .user(request.session().attribute("user"))
                    .build();
        }
        boolean success = service.create(contact);
        if(acceptsJson(request)) {
            logger.info("Sending JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Rendering the newly created contact template");
            return render(Templates.CONTACT, getOutcomeMap(success));
        }
    }

    @Override
    public String delete(Request request, Response response) {
        logger.info("Delete contact");
        int id = Integer.parseInt(request.params("id"));
        boolean success = service.delete(id);
        if(acceptsJson(request)) {
            logger.info("Sending JSON response");
            return getOutcomeJson(success);
        } else {
            logger.info("Rendering user's dashboard");
            return render(Templates.DASHBOARD, getOutcomeMap(success));
        }
    }

    @Override
    protected ContactService getService() {
        return new ContactService();
    }
}
