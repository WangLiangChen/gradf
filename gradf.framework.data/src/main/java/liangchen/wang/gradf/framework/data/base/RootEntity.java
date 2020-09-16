package liangchen.wang.gradf.framework.data.base;


import com.esotericsoftware.reflectasm.MethodAccess;
import liangchen.wang.crdf.framework.commons.object.EnhancedObject;
import liangchen.wang.crdf.framework.commons.utils.ContextUtil;

import java.util.List;

/**
 * @author LiangChen.Wang
 */
public abstract class RootEntity extends EnhancedObject {

    public void initOperator() {
        MethodAccess methodAccess = methodAccess();
        List<String> methodNames = methodNames();
        methodNames.forEach(methodName -> {
            if (!methodName.equals("setCreator") && !methodName.equals("setModifier")) {
                return;
            }
            Long operator = ContextUtil.INSTANCE.getOperator();
            methodAccess.invoke(this, methodName, operator);
        });
    }
}
