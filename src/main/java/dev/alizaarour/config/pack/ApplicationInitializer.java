package dev.alizaarour.config.pack;

import dev.alizaarour.utils.DataSchema;

public abstract class ApplicationInitializer {

    public static DataSchema dataSchema;

    public final void initializeApplication() throws Exception {
        loadProperties();
        deserializeData();
        loadUI();
    }

    protected abstract void loadProperties() throws Exception;

    protected abstract void deserializeData() throws Exception;

    protected abstract void loadUI() throws Exception;
}
