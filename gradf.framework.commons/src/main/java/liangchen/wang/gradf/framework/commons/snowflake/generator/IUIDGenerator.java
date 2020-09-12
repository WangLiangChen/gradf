package liangchen.wang.gradf.framework.commons.snowflake.generator;

/**
 * @author LiangChen.Wang
 */
public interface IUIDGenerator {
    Long getUID();

    String parseUID(Long uid);
}
