package liangchen.wang.gradf.framework.data.annotation;

import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.NetUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
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
        private final Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("jdbc.properties");
        private static boolean loaded = false;

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (loaded) {
                return new String[0];
            }
            Printer.INSTANCE.prettyPrint("开启了Jdbc，尝试连接Mysql......");
            String host = configuration.getString("default.host");
            int port = configuration.getInt("default.port", 0);
            if (StringUtil.INSTANCE.isBlank(host) || port <= 0) {
                throw new InfoException("Jdbc配置错误,请检查,host;{},port:{}", host, port);
            }
            boolean connectable = NetUtil.INSTANCE.isConnectable(host, port, TIMEOUT);
            if (!connectable) {
                throw new InfoException("连接Mysql失败，不能初始化Mysql配置，host;{},port:{}", host, port);
            }
            String[] imports = new String[]{DynamicDataSourceRegister.class.getName(), JdbcAutoConfiguration.class.getName()};
            Printer.INSTANCE.prettyPrint("连接Mysql成功");
            loaded = true;
            // 设置全局jdbc状态
            DataStatus.INSTANCE.setJdbcEnable(true);
            return imports;
        }
    }
}
