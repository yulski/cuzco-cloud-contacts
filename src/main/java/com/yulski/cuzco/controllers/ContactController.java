package com.yulski.cuzco.controllers;

import com.yulski.cuzco.models.Contact;
import spark.template.jtwig.JtwigTemplateEngine;

public class ContactController extends ModelController<Contact> {

    public ContactController(JtwigTemplateEngine jtwigEngine) {
        super(jtwigEngine);
    }

    @Override
    public String getOne(int id) {
        return null;
    }

    public String getAllForUser(int userId) {
        return null;
    }

    @Override
    public String getEditForm(int id) {
        return null;
    }

    @Override
    public String edit(int id) {
        return null;
    }

    @Override
    public String getCreateForm() {
        return null;
    }

    @Override
    public String create(Contact contact) {
        return null;
    }

    @Override
    public String delete(int id) {
        return null;
    }
}
