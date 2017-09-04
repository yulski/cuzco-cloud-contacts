package com.yulski.cuzco.util;


import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.Map;

public class Renderer {

    private ModelFactory modelFactory;
    private TemplateFactory templateFactory;

    public Renderer(ModelFactory modelFactory, TemplateFactory templateFactory) {
        this.modelFactory = modelFactory;
        this.templateFactory = templateFactory;
    }

    public String render(String templatePath, Map<String, Object> map) {
        JtwigModel model = modelFactory.createModel(map);
        JtwigTemplate template = templateFactory.createTemplate(templatePath);
        return template.render(model);
    }
}
