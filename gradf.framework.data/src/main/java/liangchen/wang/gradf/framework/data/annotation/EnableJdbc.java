package liangchen.wang.gradf.framework.data.annotation;

import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.NetUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.configuration.JdbcAutoConfiguration;
import liangchen.wang.gradf.framework.data.datasource.DynamicDataSourceRegister;
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
        private final int TIMEOUT = 30 * 1000;
        private final Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("datasource.properties");
        private static boolean loaded = false;

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (loaded) {
                return new String[0];
            }
            Printer.INSTANCE.prettyPrint("开启了Jdbc，尝试连接Mysql......");
            validateConnectionalbe();
            Printer.INSTANCE.prettyPrint("连接Mysql成功");
            String[] imports = new String[]{DynamicDataSourceRegister.class.getName(), JdbcAutoConfiguration.class.getName()};
            loaded = true;
            // 设置全局jdbc状态
            DataStatus.INSTANCE.setJdbcEnable(true);
            return imports;
        }

        private void validateConnectionalbe() {
            // 将配置分组
            Iterator<String> keys = configuration.getKeys();
            Map<String, Map<String, String>> datasourceMap = new HashMap<>();
            keys.forEachRemaining(key -> {
                String[] array = key.split("\\.");
                Map<String, String> properties = datasourceMap.get(array[0]);
                if (null == properties) {
                    properties = new HashMap<>();
                    datasourceMap.put(array[0], properties);
                }
                properties.put(array[1], configuration.getString(key));
            });
            Assert.INSTANCE.isTrue(datasourceMap.containsKey("default"), "default datasource is not exists");

            // 验证配置项是否缺失
            List<String> requiredKeyList = new ArrayList<>(Arrays.asList(new String[]{"dialect", "datasource", "host", "port", "database", "username", "password"}));
            String requiredKey = requiredKeyList.stream().sorted().collect(Collectors.joining(","));
            datasourceMap.forEach((k, v) -> {
                String configuredKey = v.keySet().stream().map(String::valueOf).sorted().collect(Collectors.joining(","));
                //Assert.INSTANCE.isTrue(configuredKey.contains(requiredKey), "配置项缺失,{}需要配置:{}", k, requiredKey);
            });
            // 验证基本网络是否通
            datasourceMap.forEach((k, v) -> {
                Assert.INSTANCE.isTrue(NetUtil.INSTANCE.isConnectable(v.get("host"), Integer.valueOf(v.get("port")), TIMEOUT), "{} 连接测试失败", k);
            });
        }
    }
}
