package liangchen.wang.gradf.framework.web.gateway;

import liangchen.wang.gradf.framework.springboot.context.BeanLoader;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author LiangChen.Wang
 */
public enum ApiContainer {
    /**
     *
     */
    INSTANCE;
    private final HashMap<String, ApiTarget> apiMap;
    private final ParameterNameDiscoverer parameterUtil;

    ApiContainer() {
        apiMap = new HashMap<>();
        parameterUtil = new LocalVariableTableParameterNameDiscoverer();
        String[] names = BeanLoader.INSTANCE.getBeanDefinitionNames();

        Class<?> type;
        ApiMapping apiMapping;
        ApiTarget apiRunnable;
        Method[] methods;
        for (String name : names) {
            type = BeanLoader.INSTANCE.getType(name);
            methods = type.getDeclaredMethods();
            for (Method method : methods) {
                apiMapping = method.getDeclaredAnnotation(ApiMapping.class);
                if (null == apiMapping) {
                    continue;
                }
                apiRunnable = new ApiTarget(apiMapping.apiName(), apiMapping.requestMethod(), name, method);
                apiMap.put(apiMapping.apiName(), apiRunnable);
            }
        }
    }

    public ApiTarget getApiTarget(String apiName) {
        return apiMap.get(apiName);
    }

    public class ApiTarget {
        // api名称
        private String apiName;
        // ioc 名称
        private String targetName;
        // 实例
        private Object target;
        // 目标方法
        private Method targetMethod;
        // 目标方法参数名
        private String[] parameterNames;
        // 目标方法参数类型
        private Class<?>[] parameterTypes;
        //请求方法
        private RequestMethod requestMethod;

        public ApiTarget(String apiName, RequestMethod requestMethod, String targetName, Method targetMethod) {
            this.apiName = apiName;
            this.targetName = targetName;
            this.targetMethod = targetMethod;
            this.requestMethod = requestMethod;
            this.target = BeanLoader.INSTANCE.getBean(targetName);
            parameterNames = parameterUtil.getParameterNames(targetMethod);
            parameterTypes = targetMethod.getParameterTypes();
            //TODO 在这里检测 方法的参数和参数的filed是不是Object类型， 是不是HashMap类型，如果是要抛出异常，这样的类型是不清晰的，对接口不友好。
        }

        public Object run(Object... args) throws IllegalAccessException, InvocationTargetException {
            return targetMethod.invoke(target, args);
        }

        public String getApiName() {
            return apiName;
        }

        public String getTargetName() {
            return targetName;
        }

        public Object getTarget() {
            return target;
        }

        public Method getTargetMethod() {
            return targetMethod;
        }

        public String[] getParameterNames() {
            return parameterNames;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public RequestMethod getRequestMethod() {
            return requestMethod;
        }

    }
}
