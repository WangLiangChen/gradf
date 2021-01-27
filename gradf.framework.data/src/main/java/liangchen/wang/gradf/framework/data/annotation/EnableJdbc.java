package liangchen.wang.gradf.framework.data.annotation;

import com.google.common.base.Splitter;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.NetUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.configuration.JdbcAutoConfiguration;
import liangchen.wang.gradf.framework.data.datasource.MultipleDataSourceRegister;
import liangchen.wang.gradf.framework.data.aspect.DynamicDataSourceAspect;
import liangchen.wang.gradf.framework.data.enumeration.DataStatus;
import org.apache.commons.configuration2.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EnableJdbc.JdbcImportSelector.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface EnableJdbc {
    class JdbcImportSelector implements ImportSelector {
        private final String JDBC_CONFIG_FILE = "jdbc.properties";
        private final int TIMEOUT = 30 * 1000;
        private static boolean loaded = false;

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (loaded) {
                return new String[0];
            }
            boolean exists = ConfigurationUtil.INSTANCE.exists(JDBC_CONFIG_FILE);
            Assert.INSTANCE.isTrue(exists, "Configuration file: {} is required,because @EnableJdbc is setted", JDBC_CONFIG_FILE);
            Printer.INSTANCE.prettyPrint("@EnableJdbc Start JDBC......");
            Printer.INSTANCE.prettyPrint("@EnableJdbc matched class: {}", annotationMetadata.getClassName());
            validateConnectionalbe();
            String[] imports = new String[]{MultipleDataSourceRegister.class.getName(), JdbcAutoConfiguration.class.getName(), DynamicDataSourceAspect.class.getName()};
            loaded = true;
            // 设置全局jdbc状态
            DataStatus.INSTANCE.setJdbcEnabled(true);
            return imports;
        }

        private void validateConnectionalbe() {
            Printer.INSTANCE.prettyPrint("Try to connect to DB......");
            Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration(JDBC_CONFIG_FILE);
            // 将配置分组
            Iterator<String> keys = configuration.getKeys();
            Map<String, Map<String, String>> datasourceMap = new HashMap<>();
            keys.forEachRemaining(key -> {
                List<String> keyList = Splitter.on('.').splitToList(key);
                String dataSourceFlag = keyList.get(0);
                datasourceMap.putIfAbsent(dataSourceFlag, new HashMap<>());
                Map<String, String> properties = datasourceMap.get(dataSourceFlag);
                properties.put(keyList.get(1), configuration.getString(key));
            });
            Assert.INSTANCE.isTrue(datasourceMap.containsKey("primary"), "primary datasource is not exists");

            // 验证配置项是否缺失
            List<String> requiredKeyList = new ArrayList<>(Arrays.asList(new String[]{"dialect", "datasource", "host", "port", "database", "username", "password"}));
            datasourceMap.forEach((k, v) -> {
                Set<String> configedSet = v.keySet().stream().collect(Collectors.toSet());
                // 取个交集
                configedSet.retainAll(requiredKeyList);
                Assert.INSTANCE.isTrue(configedSet.size() == requiredKeyList.size(), "DataSource: {}, configuration items :{} are required!", k, requiredKeyList);
            });
            // 验证基本网络是否通
            datasourceMap.forEach((k, v) -> {
                Assert.INSTANCE.isTrue(NetUtil.INSTANCE.isConnectable(v.get("host"), Integer.valueOf(v.get("port")), TIMEOUT), "DataSource {} Connection test failed", k);
            });
            Printer.INSTANCE.prettyPrint("DB Connection test successed......");
        }
    }
}
