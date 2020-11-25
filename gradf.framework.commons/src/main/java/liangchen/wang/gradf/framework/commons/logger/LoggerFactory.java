package liangchen.wang.gradf.framework.commons.logger;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author LiangChen.Wang 2020/11/24
 */
public class LoggerFactory {
    private static final ConcurrentMap<String, Logger> loggerContainer = new ConcurrentHashMap<>();

    public static Logger getLogger() {
        return new Logger(LoggerFactory.class);
    }

    public static Logger getLogger(String className) {
        Logger logger = loggerContainer.get(className);
        if (logger == null) {
            loggerContainer.putIfAbsent(className, new Logger(className));
            logger = loggerContainer.get(className);
        }
        return logger;
    }

    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }
}
