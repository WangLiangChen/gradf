package liangchen.wang.gradf.framework.data.entity;

import java.util.List;

/**
 * @author LiangChen.Wang 2019/11/19 21:07
 */
public class Columns {
    private final List<Column> primaryKeys;
    private final List<Column> columns;

    public Columns(List<Column> primaryKeys, List<Column> columns) {
        this.primaryKeys = primaryKeys;
        this.columns = columns;
    }

    public static Columns newInstance(List<Column> primaryKeys, List<Column> columns) {
        return new Columns(primaryKeys, columns);
    }

    public List<Column> getPrimaryKeys() {
        return primaryKeys;
    }

    public List<Column> getColumns() {
        return columns;
    }

}
