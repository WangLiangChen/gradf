package liangchen.wang.gradf.framework.commons.snowflake.generator;

public interface IGenerator {
	Long getUID();
	String parseUID(Long uid);
}
