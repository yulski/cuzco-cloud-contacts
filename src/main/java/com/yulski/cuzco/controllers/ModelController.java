package com.yulski.cuzco.controllers;

import com.yulski.cuzco.models.Model;
import com.yulski.cuzco.services.Service;
import spark.template.jtwig.JtwigTemplateEngine;

public abstract class ModelController<T extends Model> {

    protected JtwigTemplateEngine jtwigEngine;
    // TODO if there's something for HTML (JTwig engine), there should be something for JSON (Jackson? or own service?)
    protected Service<T> service;

    public ModelController(JtwigTemplateEngine jtwigEngine) {
        this.jtwigEngine = jtwigEngine;
    }

    public abstract String getOne(int id);

    public abstract String getEditForm(int id);

    public abstract String edit(int id);

    public abstract String getCreateForm();

    public abstract String create(T t);

    public abstract String delete(int id);

}
