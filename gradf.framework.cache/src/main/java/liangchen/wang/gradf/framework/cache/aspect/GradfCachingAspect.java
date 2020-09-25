package liangchen.wang.gradf.framework.cache.aspect;

import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.cache.annotation.GradfCacheClear;
import liangchen.wang.gradf.framework.cache.annotation.GradfCacheEvict;
import liangchen.wang.gradf.framework.cache.annotation.GradfCacheable;
import liangchen.wang.gradf.framework.cache.enumeration.MethodType;
import liangchen.wang.gradf.framework.cache.primary.DefaultKeyGenerator;
import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.cache.primary.GradfCacheManager;
import liangchen.wang.gradf.framework.commons.digest.HashUtil;
import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.json.JsonMap;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.lock.LocalLockUtil;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.commons.utils.RandomUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.AntPathMatcher;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GradfCachingAspect {
    private final Logger logger = LoggerFactory.getLogger(GradfCachingAspect.class);
    private final String _CLASS = "_class";
    private final String _LIST = "_list";
    private final String _SET = "_set";
    private final String _PAGINATION = "_pagination";
    private final String _JAVABEAN = "_javabean";
    private final GradfCacheManager cacheManager;
    private final AntPathMatcher matcher = new AntPathMatcher();
    private final Map<String, MethodType> methodCache = new HashMap<>();
    private final String _NullValue = "_NullValue";

    @Inject
    public GradfCachingAspect(@Lazy GradfCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Pointcut("@annotation(liangchen.wang.gradf.framework.cache.annotation.GradfCacheable) && execution(public !void *(..))")
    public void pointcutCacheable() {
    }

    @Pointcut("@annotation(liangchen.wang.gradf.framework.cache.annotation.GradfCacheEvict)")
    public void pointcutCacheEvict() {
    }

    @Pointcut("@annotation(liangchen.wang.gradf.framework.cache.annotation.GradfCacheClear)")
    public void pointcutCacheClear() {
    }

    @Pointcut("(@within(liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable) || target(liangchen.wang.gradf.framework.data.base.AbstractDao)) " +
            "&& !@annotation(liangchen.wang.gradf.framework.cache.annotation.GradfCacheable) " +
            "&& !@annotation(liangchen.wang.gradf.framework.cache.annotation.GradfCacheClear) " +
            "&& !@annotation(liangchen.wang.gradf.framework.cache.annotation.GradfCacheEvict)")
    public void pointcutAutoCacheable() {
    }

    @Around("pointcutAutoCacheable()")
    public Object aroundAutoCacheable(ProceedingJoinPoint point) throws Throwable {
        if (null == cacheManager) {
            logger.info("cacheManage is not be injected");
            return point.proceed();
        }
        Object target = point.getTarget();
        Class<?> clazz = target.getClass();
        GradfAutoCacheable autoCacheable = clazz.getAnnotation(GradfAutoCacheable.class);
        if (null == autoCacheable) {
            return point.proceed();
        }
        String className = clazz.getName();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String methodName = methodSignature.getName();
        logger.debug("通过@GradfAutoCacheable切入{}-{}", className, methodName);
        MethodType methodType = cachedMethodType(className, methodName, autoCacheable);
        if (MethodType.ExcludeMethod == methodType) {
            logger.debug("方法：{}被忽略", methodName);
            return point.proceed();
        }
        //缓存名称
        String cacheName = resolveCacheName(clazz, null);
        if (MethodType.ClearMethod == methodType) {
            logger.debug("方法:{}清除缓存:{}", methodName, cacheName);
            Optional<GradfCache> cache = cacheManager.getCacheIfPresent(cacheName);
            //先失效缓存,后执行方法，这样最坏就是增加一次cache miss，不会造成cache脏读
            cache.ifPresent(Cache::clear);
            return point.proceed();
        }
        //putCache
        Method method = methodSignature.getMethod();
        //缓存Key
        String key = resolveKey(null, target, method, methodSignature.getParameterNames(), point.getArgs());
        Long operator = ContextUtil.INSTANCE.getOperator();
        operator = null == operator ? 0L : operator;
        key = String.format("%s:%d", key, operator);
        String jsonString = getOrPutCache(point, cacheName, key, autoCacheable.timeUnit(), autoCacheable.duration(), autoCacheable.durationRange());
        return parseReturnObject(jsonString);
    }


    @Around("pointcutCacheable()")
    public Object aroundCacheable(ProceedingJoinPoint point) throws Throwable {
        //获取方法上的注解
        Object target = point.getTarget();
        Class<?> clazz = target.getClass();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        logger.debug("通过@GradfCacheable切入{}-{}", clazz.getName(), methodName);
        GradfCacheable cacheable = method.getAnnotation(GradfCacheable.class);
        //缓存名称
        String cacheName = cacheable.cacheName();
        cacheName = resolveCacheName(clazz, cacheName);

        //缓存Key
        String key = cacheable.key();
        key = resolveKey(key, target, method, methodSignature.getParameterNames(), point.getArgs());
        String jsonString = getOrPutCache(point, cacheName, key, cacheable.timeUnit(), cacheable.duration(), cacheable.durationRange());
        //无法获取泛型方法返回值类型，反序列化不可用 Type returnType = method.getGenericReturnType();
        return parseReturnObject(jsonString);
        // 也可用如下的spring工具获取参数名称
        // LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        // String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
    }

    @Around("pointcutCacheClear()")
    public Object aroundCacheClear(ProceedingJoinPoint point) throws Throwable {
        Object target = point.getTarget();
        Class<?> clazz = target.getClass();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        logger.debug("通过@GradfCacheClear切入{}-{}", clazz.getName(), methodName);

        GradfCacheClear annotation = method.getAnnotation(GradfCacheClear.class);
        String cacheName = annotation.cacheName();
        cacheName = resolveCacheName(clazz, cacheName);
        Optional<GradfCache> cache = cacheManager.getCacheIfPresent(cacheName);
        //先失效缓存,后执行方法，这样最坏就是增加一次cache miss，不会造成cache脏读
        cache.ifPresent(Cache::clear);
        return point.proceed();
    }

    @Around("pointcutCacheEvict()")
    public Object aroundCacheEvict(ProceedingJoinPoint point) throws Throwable {
        //获取方法或类上的注解
        Object target = point.getTarget();
        Class<?> clazz = target.getClass();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        logger.debug("通过@GradfCacheEvict切入方法:{}", methodName);

        GradfCacheEvict[] annotations = method.getAnnotationsByType(GradfCacheEvict.class);
        Arrays.stream(annotations).forEach(p -> {
            String cacheName = p.cacheName();
            cacheName = resolveCacheName(clazz, cacheName);
            Cache cache = cacheManager.getCache(cacheName);
            String key = p.key();
            key = resolveKey(key, target, method, methodSignature.getParameterNames(), point.getArgs());
            cache.evict(key);
        });
        //先失效缓存,后执行方法，这样最坏就是增加一次cache miss，不会造成cache脏读
        return point.proceed();
    }

    private String getOrPutCache(ProceedingJoinPoint point, String cacheName, String key, TimeUnit timeUnit, long duration, String durationRange) throws Throwable {
        durationRange = StringUtil.INSTANCE.clearBlank(durationRange);
        if (StringUtil.INSTANCE.isNotBlank(durationRange)) {
            String[] range = durationRange.split(Symbol.HYPHEN.getSymbol());
            int min = Integer.parseInt(range[0]);
            int max = Integer.parseInt(range[1]);
            duration = RandomUtil.INSTANCE.random(min, max);
            logger.debug("根据设置的过期时间范围:{},获取的随机数:{}", durationRange, duration);
        }
        GradfCache cache = cacheManager.getCache(cacheName, duration, timeUnit);
        // 加锁，防止并发多次穿透到业务层，如DB
        return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(key, () -> {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            // 说明无对应的Key
            if (null == valueWrapper) {
                return null;
            }
            Object result = valueWrapper.get();
            // 说明缓存的值是null
            if (null == result) {
                return _NullValue;
            }
            return String.valueOf(result);
        }, () -> {
            // 缓存不存在，从业务获取数据并更新缓存
            Object proceed;
            try {
                proceed = point.proceed();
            } catch (Throwable throwable) {
                throw new ErrorException(throwable);
            }
            if (null == proceed) {
                cache.put(key, null);
                return _NullValue;
            }
            Map<String, Object> jsonMap = new HashMap<>();
            //List
            if (proceed instanceof List) {
                List list = (List) proceed;
                if (list.size() == 0) {
                    jsonMap.put(_CLASS, "");
                } else {
                    jsonMap.put(_CLASS, list.get(0).getClass().getName());
                }
                jsonMap.put(_LIST, proceed);
                String jsonString = JsonUtil.INSTANCE.toJsonString(jsonMap);
                cache.put(key, jsonString);
                return jsonString;
            }
            //Set
            if (proceed instanceof Set) {
                Set set = (Set) proceed;
                if (set.size() == 0) {
                    jsonMap.put(_CLASS, "");
                } else {
                    jsonMap.put(_CLASS, set.iterator().next().getClass().getName());
                }
                jsonMap.put(_SET, proceed);
                String jsonString = JsonUtil.INSTANCE.toJsonString(jsonMap);
                cache.put(key, jsonString);
                return jsonString;
            }

            //PaginationResult 类型
            if (proceed instanceof PaginationResult) {
                PaginationResult paginationResult = (PaginationResult) proceed;
                List datas = paginationResult.getDatas();
                if (datas.size() == 0) {
                    jsonMap.put(_CLASS, "");
                } else {
                    jsonMap.put(_CLASS, datas.get(0).getClass().getName());
                }
                jsonMap.put(_PAGINATION, proceed);
                String jsonString = JsonUtil.INSTANCE.toJsonString(jsonMap);
                cache.put(key, jsonString);
                return jsonString;
            }
            //单对象类型
            jsonMap.put(_CLASS, proceed.getClass().getName());
            jsonMap.put(_JAVABEAN, proceed);

            String jsonString = JsonUtil.INSTANCE.toJsonString(jsonMap);
            cache.put(key, jsonString);
            return jsonString;
        });
    }

    @SuppressWarnings("unchecked")
    private Object parseReturnObject(String jsonString) {
        if (_NullValue.equals(jsonString)) {
            return null;
        }
        if (StringUtil.INSTANCE.isBlank(jsonString)) {
            return jsonString;
        }
        JsonMap jsonMap = JsonUtil.INSTANCE.parseJsonMap(jsonString);
        String _classString = jsonMap.getString(_CLASS);

        Class<?> _class = null;
        if (StringUtil.INSTANCE.isNotBlank(_classString)) {
            try {
                _class = Class.forName(_classString);
            } catch (ClassNotFoundException e) {
                throw new ErrorException(e);
            }
        }

        //处理JavaBean
        if (jsonMap.containsKey(_JAVABEAN)) {
            return JsonUtil.INSTANCE.parseObject(jsonMap.getString(_JAVABEAN), _class);
        }
        //处理List
        if (jsonMap.containsKey(_LIST)) {
            return JsonUtil.INSTANCE.parseList(jsonMap.getJSONList(_LIST), _class);
        }
        //处理Set
        if (jsonMap.containsKey(_SET)) {
            List<?> list = JsonUtil.INSTANCE.parseList(jsonMap.getJSONList(_SET), _class);
            return new HashSet<>(list);
        }
        //处理PaginationResult
        if (jsonMap.containsKey(_PAGINATION)) {
            Object paginationResult = JsonUtil.INSTANCE.parseParameterizedObject(jsonMap.getJSONMap(_PAGINATION), PaginationResult.class, _class);
            return paginationResult;
        }
        throw new InfoException("暂不支持缓存此格式的数据");
    }

    private String resolveCacheName(Class clazz, String cacheName) {
        if (StringUtil.INSTANCE.isBlank(cacheName)) {
            cacheName = clazz.getName();
        }
        String md5 = md5(cacheName);
        logger.debug("cacheName:{},md5:{}", cacheName, md5);
        return md5;
    }

    private String resolveKey(String key, Object target, Method method, String[] parameterNames, Object[] args) {
        if (StringUtil.INSTANCE.isBlank(key)) {
            DefaultKeyGenerator keyGenerator = new DefaultKeyGenerator();
            Object object = keyGenerator.generate(target, method, args);
            key = String.valueOf(object);
            String md5 = md5(key);
            logger.debug("cacheKey:{},md5:{}", key, md5);
            return md5;
        }

        ExpressionParser expressionParser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            evaluationContext.setVariable(parameterNames[i], args[i]);
        }
        //这里key必须为spel格式，纯字符串格式：'WangLiangChen'
        Expression expression = expressionParser.parseExpression(key);
        key = expression.getValue(evaluationContext, String.class);
        String md5 = md5(key);
        logger.debug("cacheKey:{},md5:{}", key, md5);
        return md5;
    }

    private MethodType cachedMethodType(String className, String methodName, GradfAutoCacheable autoCacheable) {
        String key = String.format("%s.%s", className, methodName);
        MethodType methodType = methodCache.get(key);
        if (null != methodType) {
            logger.debug("find cached method:{}.{}", className, methodName);
            return methodType;
        }
        // 创建MethodType
        synchronized (key.intern()) {
            methodType = methodCache.get(key);
            if (null != methodType) {
                return methodType;
            }
            MethodType newMethodType = methodType(methodName, autoCacheable);
            methodCache.put(key, newMethodType);
            return newMethodType;
        }
    }

    private MethodType methodType(String methodName, GradfAutoCacheable autoCacheable) {
        //excludeMethods,排除的方法兼容逗号隔开的形式，数组的每一个元素可以是逗号隔开的字符串
        String[] excludeMethods = autoCacheable.excludeMethods();
        String[] innerMethods;
        for (String element : excludeMethods) {
            innerMethods = element.split(",");
            for (String innerElement : innerMethods) {
                if (matcher.matchStart(innerElement, methodName)) {
                    return MethodType.ExcludeMethod;
                }
            }
        }
        //clearMethods 兼容逗号隔开的形式
        String[] clearMethods = autoCacheable.clearMethods();
        for (String element : clearMethods) {
            innerMethods = element.split(",");
            for (String innerElement : innerMethods) {
                if (matcher.matchStart(innerElement, methodName)) {
                    return MethodType.ClearMethod;
                }
            }
        }
        return MethodType.NormalMethod;
    }

    private String md5(String string) {
        if (string.length() > 32) {
            return HashUtil.INSTANCE.md5Digest(string);
        }
        return string;
    }
}
