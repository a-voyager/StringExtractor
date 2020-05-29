package top.wuhaojie.se.config;

import com.intellij.ide.util.PropertiesComponent;
import top.wuhaojie.se.entity.StringContainerFileType;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private static Config config;

    private Map<String, String> template = new HashMap<>();

    private String prefix = "";

    public void putPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void putTemplate(StringContainerFileType fileType, String value) {
        template.put(fileType.getFileTypeName(), value);
    }

    public String getTemplate(StringContainerFileType fileType) {
        String key = fileType.getFileTypeName();
        if (!template.containsKey(key)) {
            return "";
        }
        return template.get(key);
    }

    private Config() {
        for (StringContainerFileType type : StringContainerFileType.values()) {
            String key = type.getFileTypeName();
            String propertiesKey = String.format("key_template_%s", key);
            String value = PropertiesComponent.getInstance().getValue(propertiesKey);
            if (value == null || value.length() <= 0) {
                continue;
            }
            template.put(key, value);
        }
        prefix = PropertiesComponent.getInstance().getValue("key_prefix", "");
    }

    public void save() {

        for (Map.Entry<String, String> entry : template.entrySet()) {
            String propertiesKey = String.format("key_template_%s", entry.getKey());
            String value = entry.getValue();
            PropertiesComponent.getInstance().setValue(propertiesKey, value);
        }

        PropertiesComponent.getInstance().setValue("key_prefix", prefix);

    }

    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }


}
