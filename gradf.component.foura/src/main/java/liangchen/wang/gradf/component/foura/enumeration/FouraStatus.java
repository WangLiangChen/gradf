package liangchen.wang.gradf.component.foura.enumeration;

import liangchen.wang.gradf.framework.data.enumeration.Status;

/**
 * @author LiangChen.Wang
 */
public class FouraStatus extends Status {
    public final static Status PRESET = new FouraStatus("PRESET", "预置");

    public FouraStatus(String name, String text) {
        super(name, text);
    }
}
