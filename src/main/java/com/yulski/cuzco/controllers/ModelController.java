package com.yulski.cuzco.controllers;

import com.yulski.cuzco.models.Model;
import com.yulski.cuzco.services.Service;
import com.yulski.cuzco.util.SessionManager;
import com.yulski.cuzco.util.Renderer;
import spark.Request;
import spark.Response;

public abstract class ModelController<T extends Model, V extends Service<T>> extends Controller {

    public ModelController(Renderer renderer, SessionManager session) {
        super(renderer, session);
    }

    protected V service = getService();

    public abstract String getOne(Request request, Response response);

    public abstract String getEditForm(Request request, Response response);

    public abstract String edit(Request request, Response response);

    public abstract String getCreateForm(Request request, Response response);

    public abstract String create(Request request, Response response);

    public abstract String getDeleteForm(Request request, Response response);

    public abstract String delete(Request request, Response response);

    protected abstract V getService();

}
