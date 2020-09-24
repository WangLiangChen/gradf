package liangchen.wang.gradf.framework.cluster.annotation;

import com.google.common.base.Splitter;
import liangchen.wang.gradf.framework.cluster.configuration.RedisAutoConfiguration;
import liangchen.wang.gradf.framework.cluster.enumeration.ClusterStatus;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.NetUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import org.apache.commons.configuration2.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.*;
import java.util.Iterator;
import java.util.List;

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
            Printer.INSTANCE.prettyPrint("开启了Redis......");
            //判断是否集群配置
            String cluster = configuration.getString("cluster.nodes");
            if (StringUtil.INSTANCE.isBlank(cluster)) {
                Printer.INSTANCE.prettyPrint("Redis配置为单机模式......");
                String host = configuration.getString("host");
                int port = configuration.getInt("port", 0);
                connectableCheck(host, port);
                String[] imports = new String[]{RedisAutoConfiguration.class.getName()};
                Printer.INSTANCE.prettyPrint("Redis单机模式连接成功");
                loaded = true;
                // 设置全局redis状态
                ClusterStatus.INSTANCE.setRedisEnable(true);
                return imports;
            }
            //集群模式
            Printer.INSTANCE.prettyPrint("Redis配置为集群模式......");
            Iterator<String> hosts = Splitter.on(",").split(cluster).iterator();
            while (hosts.hasNext()) {
                String next = hosts.next();
                List<String> hostAndPorts = Splitter.on(":").splitToList(next);
                connectableCheck(hostAndPorts.get(0), Integer.parseInt(hostAndPorts.get(1)));
            }
            String[] imports = new String[]{RedisAutoConfiguration.class.getName()};
            Printer.INSTANCE.prettyPrint("Redis集群模式连接成功");
            loaded = true;
            // 设置全局redis状态
            ClusterStatus.INSTANCE.setRedisEnable(true);
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
