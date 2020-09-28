package liangchen.wang.gradf.component.foura.enumeration;

/**
 * @author LiangChen.Wang 2019/10/11 9:42
 */
public enum FouraDefaultRoles {
    //
    ROOT("超级管理员"), ADMIN("管理员");

    private final String text;

    FouraDefaultRoles(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
