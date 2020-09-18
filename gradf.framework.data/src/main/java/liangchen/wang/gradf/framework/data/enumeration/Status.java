package liangchen.wang.gradf.framework.data.enumeration;


import liangchen.wang.gradf.framework.commons.enumeration.BaseEnum;

/**
 * @author LiangChen.Wang
 */
public class Status extends BaseEnum {
    public final static Status NONE = new Status("NONE", "无状态");
    public final static Status INITIAL = new Status("INITIAL", "初始");
    public final static Status PENDING = new Status("PENDING", "待审核");
    public final static Status REJECT = new Status("REJECT", "驳回");
    public final static Status SUSPEND = new Status("SUSPEND", "暂停");
    public final static Status NORMAL = new Status("NORMAL", "正常");
    public final static Status DELETED = new Status("DELETED", "删除");
    public final static Status INPROGRESS = new Status("INPROGRESS", "进行中");
    public final static Status FINISH = new Status("FINISH", "完成");
    public final static Status FAILED = new Status("FAILED", "失败");

    public Status(String name, String text) {
        super(name, text);
    }

    public static String[] names(Status[] statuses) {
        if (null == statuses) {
            return null;
        }
        String[] names = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            names[i] = statuses[i].name();
        }
        return names;
    }
}
