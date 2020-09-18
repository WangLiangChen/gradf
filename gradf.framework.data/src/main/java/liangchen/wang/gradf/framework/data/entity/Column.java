package liangchen.wang.gradf.framework.data.entity;

import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;

/**
 * @author LiangChen.Wang 2019/11/6 17:10
 */
public class Column implements Cloneable {
    private static final Column self = new Column();
    private String columnName;
    private String jdbcTypeName;
    private String javaTypeName;
    private Class<?> javaType;
    private boolean primaryKey;
    private String fieldName;

    public static Column newInstance(String columnName, String jdbcTypeName, String javaTypeName) {
        return newInstance(columnName, jdbcTypeName, javaTypeName, false);
    }

    public static Column newInstance(String columnName, String jdbcTypeName, String javaTypeName, boolean primaryKey) {
        Column column = newInstance();
        column.setPrimaryKey(primaryKey);
        column.setColumnName(columnName);
        column.setJdbcTypeName(jdbcTypeName);
        Class<?> javaType = ClassBeanUtil.INSTANCE.forName(javaTypeName);
        column.setJavaType(javaType);
        if (javaTypeName.startsWith("java.lang.")) {
            javaTypeName = javaTypeName.substring(10);
        }
        column.setJavaTypeName(javaTypeName);

        if (!columnName.contains(Symbol.UNDERLINE.getSymbol())) {
            column.setFieldName(columnName);
        } else {
            String[] split = columnName.split(Symbol.UNDERLINE.getSymbol());
            for (int i = 1; i < split.length; i++) {
                String word = split[i];
                split[i] = StringUtil.INSTANCE.firstLetterUpperCase(word);
            }
            column.setFieldName(String.join("", split));
        }
        return column;
    }

    private static Column newInstance() {
        try {
            return ClassBeanUtil.INSTANCE.cast(self.clone());
        } catch (CloneNotSupportedException e) {
            throw new ErrorException(e);
        }
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public String getJavaTypeName() {
        return javaTypeName;
    }

    public void setJavaTypeName(String javaTypeName) {
        this.javaTypeName = javaTypeName;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
