package liangchen.wang.gradf.framework.commons.enumeration;

/**
 * @author LiangChen.Wang
 */

public enum ExceptionCode {
    /**
     *
     */
    NeedLogin("需要登录");
    private final String text;

    ExceptionCode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
