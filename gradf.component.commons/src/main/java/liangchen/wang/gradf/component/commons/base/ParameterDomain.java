package liangchen.wang.gradf.component.commons.base;

import liangchen.wang.gradf.framework.commons.object.EnhancedObject;
import liangchen.wang.gradf.framework.data.core.RootEntity;

import java.util.function.Consumer;

/**
 * @author LiangChen.Wang 2019/11/21 10:06
 */
public class ParameterDomain<E extends RootEntity> extends EnhancedObject {

    private Consumer<E> entityConsumer;

    public Consumer<E> getEntityConsumer() {
        return entityConsumer;
    }

    public void populateEntity(Consumer<E> entityConsumer) {
        this.entityConsumer = entityConsumer;
    }

}
