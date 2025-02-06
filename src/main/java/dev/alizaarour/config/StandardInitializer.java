package dev.alizaarour.config;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.utils.DataAction;
import dev.alizaarour.views.Login;

import javax.swing.*;

public class StandardInitializer extends ApplicationInitializer {

    @Override
    protected void loadProperties() throws Exception {
        StandardApplicationProperties.getInstance().init();
    }

    @Override
    protected void deserializeData() throws Exception {
        dataSchema = DataAction.deserialize (StandardApplicationProperties.getInstance().getDataPath() + "/data_schema.ser");
    }


    @Override
    protected void loadUI() throws Exception {
        if (StandardApplicationProperties.getInstance().getAppStyle().equalsIgnoreCase("light"))
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        else if (StandardApplicationProperties.getInstance().getAppStyle().equalsIgnoreCase("dark"))
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        else throw new ClassNotFoundException();

        new Login();
    }
}
