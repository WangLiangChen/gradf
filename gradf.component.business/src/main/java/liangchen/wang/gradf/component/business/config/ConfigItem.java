package liangchen.wang.gradf.component.business.config;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.object.EnhancedObject;

public class ConfigItem extends EnhancedObject {

    private static final ConfigItem self = new ConfigItem();

    public static ConfigItem newInstance(String key, String value) {
        return newInstance(key, value, null);
    }

    public static ConfigItem newInstance(String key, String value, String summary) {
        ConfigItem configItem = ClassBeanUtil.INSTANCE.cast(self.clone());
        configItem.key = key;
        configItem.value = value;
        configItem.summary = summary;
        return configItem;
    }

    private String key;
    private String value;
    private String summary;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getSummary() {
        return summary;
    }
}
