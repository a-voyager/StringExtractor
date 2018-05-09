package top.wuhaojie.se.config;

import com.intellij.ide.util.PropertiesComponent;

public class Config {

    private static Config config;

    private boolean key;

    private Config() {

    }

    public void save() {

        PropertiesComponent.getInstance().setValue("key", "value");
    }

    public static Config getInstant() {

        if (config == null) {
            config = new Config();
            config.key = PropertiesComponent.getInstance().getBoolean("fieldPrivateMode", true);
        }
        return config;
    }


}
