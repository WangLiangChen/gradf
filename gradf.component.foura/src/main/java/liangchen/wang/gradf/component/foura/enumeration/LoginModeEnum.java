package liangchen.wang.gradf.component.foura.enumeration;

import liangchen.wang.gradf.component.commons.config.ConfigItem;

/**
 * @author LiangChen.Wang
 */
public enum LoginModeEnum {
    //
    PASSWORD("用户密码") {
        @Override
        public ConfigItem[] configItems() {
            return new ConfigItem[]{
                    ConfigItem.newInstance("password", "密码"),
                    ConfigItem.newInstance("salt", "密码盐"),
                    ConfigItem.newInstance("jwtSecretKey", "jwt加密密钥")};
        }
    }, MOBILE("手机验证码") {
        @Override
        public ConfigItem[] configItems() {
            return new ConfigItem[0];
        }
    }, EMAIL("邮箱验证码") {
        @Override
        public ConfigItem[] configItems() {
            return new ConfigItem[0];
        }
    }, WECHAT("微信") {
        @Override
        public ConfigItem[] configItems() {
            return new ConfigItem[0];
        }
    };

    private final String modeText;

    LoginModeEnum(String modeText) {
        this.modeText = modeText;
    }

    public abstract ConfigItem[] configItems();
}
