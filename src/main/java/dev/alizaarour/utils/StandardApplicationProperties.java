package dev.alizaarour.utils;

import lombok.Getter;

import java.util.Properties;


@Getter
public class StandardApplicationProperties implements ApplicationProperties {

    private static StandardApplicationProperties applicationProperties;
    private Properties properties;
    private String appStyle;

    private StandardApplicationProperties() {}

    public static StandardApplicationProperties getInstance() {
        if (applicationProperties == null) applicationProperties = new StandardApplicationProperties();
        return applicationProperties;
    }

    public Object clone() throws CloneNotSupportedException{
        throw new CloneNotSupportedException();
    }

    @Override
    public void init() throws Exception {
        properties = new Properties();
        properties.load(StandardApplicationProperties.class.getClassLoader().getResourceAsStream("application.properties"));
        appStyle =  properties.getProperty("app.style");
    }
}
