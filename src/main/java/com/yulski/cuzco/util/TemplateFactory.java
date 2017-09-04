package com.yulski.cuzco.util;


import org.jtwig.JtwigTemplate;
import org.jtwig.environment.Environment;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.environment.EnvironmentFactory;
import org.jtwig.resource.reference.ResourceReference;

import java.io.File;

public class TemplateFactory {

    private Environment env;
    private String templatesDir;
    private JTwigFunctions jTwigFunctions;

    public TemplateFactory(String templatesDir, JTwigFunctions jTwigFunctions) {
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
    }

    JtwigTemplate createTemplate(String templatePath) {
        ResourceReference resource = new ResourceReference(
                ResourceReference.FILE,
                templatesDir + File.separator + templatePath
        );
        return new JtwigTemplate(env, resource);
    }

}
