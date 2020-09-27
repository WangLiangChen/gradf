package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.exeception.ErrorException;

import java.io.Serializable;

public abstract class FouraInitialization implements Serializable, Cloneable {
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ErrorException(e);
        }
    }
}
