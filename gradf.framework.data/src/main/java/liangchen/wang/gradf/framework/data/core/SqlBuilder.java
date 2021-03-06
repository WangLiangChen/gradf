package liangchen.wang.gradf.framework.data.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiangChen.Wang
 */
public class SqlBuilder {
    private StringBuffer sql;
    private List<Object> args;
    private List<Object[]> batchArgs;
    private SqlBuilder countSqlBuilder;

    public static SqlBuilder newInstance() {
        return new SqlBuilder();
    }

    public SqlBuilder() {
        this.sql = new StringBuffer();
        this.args = new ArrayList<Object>();
    }

    public void reset() {
        this.sql.setLength(0);
        this.args.clear();
    }

    public SqlBuilder(String s) {
        this();
        this.sql.append(s);
    }

    public SqlBuilder append(String s) {
        this.sql.append(s);
        return this;
    }

    public SqlBuilder appendArgs(Object e) {
        this.args.add(e);
        return this;
    }

    public Object[] getArgs() {
        return args.toArray();
    }

    public String getSql() {
        return sql.toString();
    }

    public SqlBuilder getCountSqlBuilder() {
        return countSqlBuilder;
    }

    public void setCountSqlBuilder(SqlBuilder countSqlBuilder) {
        this.countSqlBuilder = countSqlBuilder;
    }

    public List<Object[]> getBatchArgs() {
        return batchArgs;
    }

    public void setBatchArgs(List<Object[]> batchArgs) {
        this.batchArgs = batchArgs;
    }
}
