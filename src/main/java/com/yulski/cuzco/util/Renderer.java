package com.yulski.cuzco.util;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.Environment;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.environment.EnvironmentFactory;
import org.jtwig.resource.reference.ResourceReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Renderer {

    private Environment env;
    private String templatesDir;
    private Map<String, Object> baseModel;
    private JTwigFunctions jTwigFunctions;

    public Renderer(String templatesDir, JTwigFunctions jTwigFunctions) {
        this.templatesDir = templatesDir;
        this.jTwigFunctions = jTwigFunctions;
        init();
    }

    private void init() {
        EnvironmentConfiguration envConfig = EnvironmentConfigurationBuilder
                .configuration()
                    .functions()
                        .add(jTwigFunctions.getPath())
                        .add(jTwigFunctions.getToString())
                    .and()
                .build();
        EnvironmentFactory envFactory = new EnvironmentFactory();
        env = envFactory.create(envConfig);
        this.baseModel = new HashMap<>();
        this.initBaseModel();
    }

    private void initBaseModel() {
        Map<String, Object> paths = Paths.getMap();
        for(String key : paths.keySet()) {
            baseModel.put("PATH_" + key, paths.get(key));
        }
    }

    public String render(String templatePath, Map<String, Object> map) {
        ResourceReference resource = new ResourceReference(
                ResourceReference.FILE,
                templatesDir + File.separator + templatePath
        );
        JtwigTemplate template = new JtwigTemplate(env, resource);
        if(map == null) {
            map = new HashMap<>();
        }
        map.putAll(baseModel);
        JtwigModel model = createModel(map);
        return template.render(model);
    }

    private JtwigModel createModel(Map<String, Object> map) {
        JtwigModel model = JtwigModel.newModel();
        for(String key : map.keySet()) {
            model.with(key, map.get(key));
        }
        return model;
    }

}
