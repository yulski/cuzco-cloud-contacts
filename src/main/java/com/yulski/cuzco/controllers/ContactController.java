package com.yulski.cuzco.controllers;

import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import com.yulski.cuzco.services.ContactService;
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

    public ContactController(JtwigTemplateEngine jtwigEngine) {
        super(jtwigEngine);
    }

    @Override
    public String getOne(Request request, Response response) {
        int id = Integer.parseInt(request.params("id"));
        Contact contact = service.getOne(id);
        if(acceptsJson(request)) {
            return gson.toJson(contact);
        } else {
            Map<String, Object> model = new HashMap<>();
            model.put("contact", contact);
            return render(model, Templates.CONTACT);
        }
    }

    public String getAllForUser(Request request, Response response) {
        User user = request.session().attribute("user");
        List<Contact> contacts = service.getContactsForUser(user);
        if(acceptsJson(request)) {
            return gson.toJson(contacts);
        } else {
            Map<String, Object> model = new HashMap<>();
            model.put("contacts", contacts);
            return render(model, Templates.DASHBOARD);
        }
    }

    @Override
    public String getEditForm(Request request, Response response) {
        int id = Integer.parseInt(request.params("id"));
        Contact contact = service.getOne(id);
        Map<String, Object> model = new HashMap<>();
        model.put("contact", contact);
        return render(model, Templates.EDIT_CONTACT);
    }

    @Override
    public String edit(Request request, Response response) {
        int id = Integer.parseInt(request.params("id"));
        Contact oldContact = service.getOne(id);
        Contact contact;
        if(isJson(request)) {
            contact = gson.fromJson(request.body(), Contact.class);
            contact.setId(id);
        } else {
            contact = Contact.builder()
                    .id(id)
                    .name(request.queryParams("name"))
                    .phoneNumber(request.queryParams("phone-number"))
                    .user(oldContact.getUser())
                    .build();
        }
        boolean success = service.update(contact);
        if(acceptsJson(request)) {
            return getOutcomeJson(success);
        } else {
            return render(getOutcomeMap(success), Templates.CONTACT);
        }
    }

    @Override
    public String getCreateForm(Request request, Response response) {
        return render(null, Templates.CREATE_CONTACT);
    }

    @Override
    public String create(Request request, Response response) {
        Contact contact;
        if(isJson(request)) {
            contact = gson.fromJson(request.body(), Contact.class);
        } else {
            contact = Contact.builder()
                    .name(request.queryParams("name"))
                    .phoneNumber(request.queryParams("phone-number"))
                    .user(request.session().attribute("user"))
                    .build();
        }
        boolean success = service.create(contact);
        if(acceptsJson(request)) {
            return getOutcomeJson(success);
        } else {
            return render(getOutcomeMap(success), Templates.CONTACT);
        }
    }

    @Override
    public String delete(Request request, Response response) {
        int id = Integer.parseInt(request.params("id"));
        boolean success = service.delete(id);
        if(acceptsJson(request)) {
            return getOutcomeJson(success);
        } else {
            return render(getOutcomeMap(success), Templates.DASHBOARD);
        }
    }

    @Override
    protected ContactService getService() {
        return new ContactService();
    }
}
