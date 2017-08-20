package com.yulski.cuzco.controllers;

import com.yulski.cuzco.models.User;
import spark.template.jtwig.JtwigTemplateEngine;

public class UserController extends ModelController<User> {

    public UserController(JtwigTemplateEngine jtwigEngine) {
        super(jtwigEngine);
    }

    @Override
    public String getOne(int id) {
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
    public String create(User user) {
        return null;
    }

    @Override
    public String delete(int id) {
        return null;
    }
}
