package liangchen.wang.gradf.component.foura.aspect;

import liangchen.wang.gradf.component.foura.annotation.EnableOperationLog;
import liangchen.wang.gradf.component.foura.annotation.OperationLog;
import liangchen.wang.gradf.component.foura.manager.IOperationLogManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.OperationLogParameterDomain;
import liangchen.wang.gradf.component.foura.utils.OperationLogUtil;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.data.enumeration.OperationEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;

@Component
@Aspect
public class OperationLogAspect {
    private final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);
    private final IOperationLogManager operationLogManager;

    @Inject
    public OperationLogAspect(@Named("Gradf_Business_DefaultOperationLogManager") IOperationLogManager operationLogManager) {
        this.operationLogManager = operationLogManager;
    }

    @Pointcut("@within(liangchen.wang.gradf.component.foura.annotation.EnableOperationLog) && @annotation(liangchen.wang.gradf.component.foura.annotation.OperationLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Throwable throwable = null;
        try {
            return point.proceed();
        } catch (Throwable e) {
            throwable = e;
            throw throwable;
        } finally {
            handleLog(point, throwable);
        }
    }

    private void handleLog(ProceedingJoinPoint point, Throwable throwable) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        OperationEnum operationEnum = operationLog.value();
        String operationFlag = operationLog.operationFlag();
        String operationName = operationLog.operationName();
        if (operationEnum != OperationEnum.NONE) {
            operationFlag = operationEnum.name();
            operationName = operationEnum.getText();
        }
        if (StringUtil.INSTANCE.isBlank(operationFlag) || StringUtil.INSTANCE.isBlank(operationName)) {
            throw new InfoException("@OperationLog必须设置operationFlag、operationName或者operationEnum");
        }
        String businessType = operationLog.businessType();
        String businessName = operationLog.businessName();
        Object target = point.getTarget();
        Class<?> clazz = target.getClass();
        if (StringUtil.INSTANCE.isBlank(businessType) || StringUtil.INSTANCE.isBlank(businessName)) {
            EnableOperationLog enableOperationLog = clazz.getAnnotation(EnableOperationLog.class);
            businessType = enableOperationLog.businessType();
            businessName = enableOperationLog.businessName();
        }
        if (StringUtil.INSTANCE.isBlank(businessType) || StringUtil.INSTANCE.isBlank(businessName)) {
            throw new InfoException("@EnableOperationLog必须设置businessType和businessName");
        }
        OperationLogParameterDomain parameterDomain = OperationLogParameterDomain.newInstance();
        parameterDomain.setBusiness_id(OperationLogUtil.INSTANCE.getBusinessId());
        parameterDomain.setBusiness_name(businessName);
        parameterDomain.setBusiness_type(businessType);
        parameterDomain.setOperation_flag(operationFlag);
        parameterDomain.setOperation_name(operationName);
        parameterDomain.setNew_data(OperationLogUtil.INSTANCE.getNewData());
        parameterDomain.setOriginal_data(OperationLogUtil.INSTANCE.getOriginalData());
        parameterDomain.setSource_class(clazz.getName());
        parameterDomain.setSource_method(method.getName());
        operationLogManager.insert(parameterDomain);
    }
}
