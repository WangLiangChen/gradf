package liangchen.wang.gradf.framework.data.annotation;

import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.configuration.CassandraAutoConfiguration;
import liangchen.wang.gradf.framework.data.enumeration.DataStatus;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EnableCassandra.CassandraImportSelector.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface EnableCassandra {
    class CassandraImportSelector implements ImportSelector {
        private final String CASSANDRA_CONFIG_FILE = "cassandra.properties";
        private final int TIMEOUT = 30 * 1000;
        private static boolean loaded = false;

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (loaded) {
                return new String[0];
            }
            boolean exists = ConfigurationUtil.INSTANCE.exists(CASSANDRA_CONFIG_FILE);
            Assert.INSTANCE.isTrue(exists, "Configuration file: {} is required,because @EnableCassandra is setted", CASSANDRA_CONFIG_FILE);
            Printer.INSTANCE.prettyPrint("@EnableJdbc Start JDBC......");
            Printer.INSTANCE.prettyPrint("@EnableJdbc matched class: {}", annotationMetadata.getClassName());
            validateConnectionalbe();
            String[] imports = new String[]{CassandraAutoConfiguration.class.getName(), "org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration"};
            loaded = true;
            DataStatus.INSTANCE.setCassandraEnabled(true);
            return imports;
        }

        private void validateConnectionalbe() {
            // 验证配置项是否缺失
            /*List<String> requiredKeyList = new ArrayList<>(Arrays.asList(new String[]{"contact-points", "port", "keyspace-name""username", "password"}));
            String requiredKey = requiredKeyList.stream().sorted().collect(Collectors.joining(","));
            String configuredKey = v.keySet().stream().sorted().limit(requiredKeyList.size()).collect(Collectors.joining(","));
            Assert.INSTANCE.isTrue(requiredKey.equals(configuredKey), "DataSource: {}, configuration items :{} are required!", k, requiredKey);
            */
            Printer.INSTANCE.prettyPrint("Cassandra Connection test successed......");
        }
    }
}
