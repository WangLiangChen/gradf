package liangchen.wang.gradf.framework.data.enumeration;

/**
 * @author LiangChen.Wang
 */
public enum DataStatus {
    /**
     *
     */
    INSTANCE;
    private boolean jdbcEnable;

    public boolean isJdbcEnable() {
        return jdbcEnable;
    }

    public boolean isNotJdbcEnable() {
        return !jdbcEnable;
    }

    public void setJdbcEnable(boolean jdbcEnable) {
        this.jdbcEnable = jdbcEnable;
    }
}
