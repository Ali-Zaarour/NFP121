package dev.alizaarour.config;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.utils.DataAction;
import dev.alizaarour.utils.DataSchema;
import dev.alizaarour.views.Login;

import javax.swing.*;
import java.io.File;

public class StandardInitializer extends ApplicationInitializer {

    @Override
    protected void loadProperties() throws Exception {
        StandardApplicationProperties.getInstance().init();
    }

    @Override
    protected void deserializeData() throws Exception {
        String filePath = StandardApplicationProperties.getInstance().getDataPath() + "/data_schema.ser";
        File file = new File(filePath);

        if (file.exists()) {
            dataSchema = DataAction.deserialize(filePath);
        } else {
            System.out.println("Data file not found, creating a new one...");
            dataSchema = new DataSchema();
            DataAction.serialize(dataSchema, filePath);
        }
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
