package liangchen.wang.gradf.framework.data.enumeration;

/**
 * @author LiangChen.Wang
 */
public enum DataStatus {
    /**
     *
     */
    INSTANCE;
    private boolean jdbcEnabled;
    private boolean cassandraEnabled;

    public boolean isJdbcEnabled() {
        return jdbcEnabled;
    }

    public boolean isNotJdbcEnabled() {
        return !jdbcEnabled;
    }

    public void setJdbcEnabled(boolean jdbcEnabled) {
        this.jdbcEnabled = jdbcEnabled;
    }

    public boolean isCassandraEnabled() {
        return cassandraEnabled;
    }

    public boolean isNotCassandraEnabled() {
        return !cassandraEnabled;
    }

    public void setCassandraEnabled(boolean cassandraEnabled) {
        this.cassandraEnabled = cassandraEnabled;
    }
}
