package liangchen.wang.gradf.framework.commons.logger.aspect;

import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.logger.annotation.LoggerAnnotation;
import liangchen.wang.gradf.framework.commons.logger.annotation.LoggerLevel;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author LiangChen.Wang
 */
@Aspect
public class LoggerAspect {
    private final static Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Pointcut("@within(liangchen.wang.gradf.framework.springboot.logger.annotation.LoggerAnnotation) || @annotation(liangchen.wang.gradf.framework.springboot.logger.annotation.LoggerAnnotation)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Class<?> clazz = point.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        LoggerAnnotation loggerAnnotation = method.getAnnotation(LoggerAnnotation.class);
        if (null == loggerAnnotation) {
            loggerAnnotation = clazz.getAnnotation(LoggerAnnotation.class);
        }
        LoggerLevel level = loggerAnnotation.value();
        StringBuilder log = new StringBuilder();
        long time = System.currentTimeMillis();
        log.append("进入方法:").append(clazz.getName()).append(".").append(method.getName());
        outputLog(level, log.toString());
        //获取方法参数名
        String[] parameterNames = methodSignature.getParameterNames();
        Class[] parameterTypes = methodSignature.getParameterTypes();
        Object[] args = point.getArgs();

        if (CollectionUtil.INSTANCE.isNotEmpty(args)) {
            log.setLength(0);
            log.append("方法参数：");
            for (int i = 0; i < args.length; i++) {
                log.append(System.getProperty("line.separator")).append("[").append(parameterTypes[i].getSimpleName()).append(" ").append(parameterNames[i]).append("]:").append(JsonUtil.INSTANCE.toJSONStringWithTransientField(args[i]));
            }
            outputLog(level, log.toString());
        }
        Class<?> returnType = method.getReturnType();
        Object retVal = point.proceed();
        log.setLength(0);
        log.append("返回值[").append(returnType.getSimpleName()).append("]:").append(JsonUtil.INSTANCE.toJSONString(retVal));
        outputLog(level, log.toString());

        time = System.currentTimeMillis() - time;
        log.setLength(0);
        log.append("离开方法:").append(clazz.getName()).append(".").append(method.getName()).append(",耗时:").append(time).append("ms");
        outputLog(level, log.toString());
        return retVal;
    }

    private void outputLog(LoggerLevel level, String log) {
        switch (level) {
            case TRACE:
                logger.trace(log);
                break;
            case DEBUG:
                logger.debug(log);
                break;
            case INFO:
                logger.info(log);
                break;
            case WARN:
                logger.warn(log);
                break;
            case ERROR:
                logger.error(log);
                break;
            default:
                logger.error("日志level设置错误");
                break;
        }
    }
}
