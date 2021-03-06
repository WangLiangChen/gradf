package liangchen.wang.gradf.framework.web.sse;

/**
 * @author LiangChen.Wang
 */
public class SseKey {
    private final String name;
    private final String group;

    public static SseKey newInstance(String name, String group) {
        return new SseKey(name, group);
    }

    public SseKey(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }
}
