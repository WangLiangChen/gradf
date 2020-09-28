package liangchen.wang.gradf.component.foura.enumeration;

/**
 * @author LiangChen.Wang
 */
public enum AuthType {
    //
    LOGIN("登录授权"), ROLE("角色授权");

    private final String text;

    AuthType(String text) {
        this.text = text;
    }

    public static AuthType valueOf(int index) {
        AuthType[] values = values();
        return values[index];
    }

    public String getText() {
        return text;
    }
}
