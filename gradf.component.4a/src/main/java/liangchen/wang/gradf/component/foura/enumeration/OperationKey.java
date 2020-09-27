package liangchen.wang.gradf.component.foura.enumeration;


import liangchen.wang.gradf.framework.commons.enumeration.BaseEnum;

/**
 * @author LiangChen.Wang
 */
public class OperationKey extends BaseEnum {
    public final static OperationKey CREATE = new OperationKey("CREATE", "新增");
    public final static OperationKey RETRIEVE = new OperationKey("RETRIEVE", "查询");
    public final static OperationKey UPDATE = new OperationKey("UPDATE", "更新");
    public final static OperationKey DELETE = new OperationKey("DELETE", "删除");

    public OperationKey(String name, String text) {
        super(name, text);
    }

    public static String[] names(OperationKey[] statuses) {
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
