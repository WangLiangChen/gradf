package liangchen.wang.gradf.component.commons.config;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.object.EnhancedObject;

public class ConfigItem extends EnhancedObject {
    private static final ConfigItem self = new ConfigItem();

    public static ConfigItem newInstance(String key, String text) {
        return newInstance(key, text, null);
    }

    public static ConfigItem newInstance(String key, String text, String summary) {
        ConfigItem configItem = ClassBeanUtil.INSTANCE.cast(self.clone());
        configItem.key = key;
        configItem.text = text;
        configItem.summary = summary;
        return configItem;
    }

    /**
     * 配置项/值 标识
     */
    private String key;
    /**
     * 配置项/值 简述
     */
    private String text;
    /**
     * 配置项/值 描述
     */
    private String summary;

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public String getSummary() {
        return summary;
    }
}
