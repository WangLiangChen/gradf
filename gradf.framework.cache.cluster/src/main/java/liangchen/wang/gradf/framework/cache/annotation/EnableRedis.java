package liangchen.wang.gradf.framework.cache.annotation;

import liangchen.wang.gradf.framework.cache.configuration.RedisAutoConfiguration;
import liangchen.wang.gradf.framework.cache.enumeration.CacheStatus;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.NetUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.apache.commons.configuration2.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EnableRedis.RedisImportSelector.class})
public @interface EnableRedis {
    class RedisImportSelector implements ImportSelector {
        private final int TIMEOUT = 30 * 1000;
        private final Configuration configuration = ConfigurationUtil.INSTANCE.getConfiguration("redis.properties");
        private static boolean loaded = false;

        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (loaded) {
                return new String[0];
            }
            Printer.INSTANCE.prettyPrint("@EnableRedis 开启了Redis......");
            Printer.INSTANCE.prettyPrint("@EnableRedis 匹配的类: {}", annotationMetadata.getClassName());
            //判断是否集群配置
            String[] nodes = configuration.getStringArray("cluster.nodes");
            Assert.INSTANCE.notEmpty(nodes, "The configuration 'nodes' is required!");
            Printer.INSTANCE.prettyPrint("Redis配置为{}模式......", nodes.length > 1 ? "集群" : "单机");
            for (String node : nodes) {
                int index = node.indexOf(':');
                connectableCheck(node.substring(0, index), Integer.parseInt(node.substring(index + 1)));
            }
            String[] imports = new String[]{RedisAutoConfiguration.class.getName()};
            Printer.INSTANCE.prettyPrint("Redis连接成功");
            loaded = true;
            // 设置全局redis状态
            CacheStatus.INSTANCE.setRedisEnable(true);
            return imports;
        }

        private void connectableCheck(String host, int port) {
            if (StringUtil.INSTANCE.isBlank(host) || port <= 0) {
                throw new InfoException("Redis配置错误,请检查,host;{},port:{}", host, port);
            }
            boolean connectable = NetUtil.INSTANCE.isConnectable(host, port, TIMEOUT);
            if (!connectable) {
                throw new InfoException("连接Redis失败，不能初始化Redis配置，host;{},port:{}", host, port);
            }
        }
    }
}
