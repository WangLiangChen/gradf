package liangchen.wang.gradf.framework.commons.generic;

import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author LiangChen.Wang
 * @date 18-12-18 上午10:31
 * XXX里含有List<T>使用下面两步
 * Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
 * Type type = new ParameterizedTypeImpl(XXX.class, new Type[]{listType});
 */
public class GradfParameterizedType implements ParameterizedType {
    private final Class raw;
    private final Type[] args;
    private final Type owner;

    public GradfParameterizedType(Class raw, Type[] args) {
        this(raw, args, null);

    }

    public GradfParameterizedType(Class raw, Type[] args, Type owner) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
        this.owner = owner;
        checkArgs();
    }

    private void checkArgs() {
        Assert.INSTANCE.notNull(raw, "raw class can't be null");
        TypeVariable[] typeParameters = raw.getTypeParameters();
        if (args.length != 0 && typeParameters.length != args.length) {
            throw new InfoException(raw.getName() + " expect " + typeParameters.length + " arg(s), got " + args.length);
        }
    }

    /**
     * @return 泛型参数，如Map<String,Integer> 返回[String,Integer]
     */
    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    /**
     * @return 如List<?>返回List
     */
    @Override
    public Type getRawType() {
        return raw;
    }

    /**
     * @return 返回所属类型. 如,当前类型为O<T>.I<S>, 则返回O<T>. 顶级类型将返回null
     */
    @Override
    public Type getOwnerType() {
        return owner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(raw.getName());
        if (args.length != 0) {
            sb.append('<');
            for (int i = 0; i < args.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                Type type = args[i];
                if (type instanceof Class) {
                    Class clazz = (Class) type;

                    if (clazz.isArray()) {
                        int count = 0;
                        do {
                            count++;
                            clazz = clazz.getComponentType();
                        } while (clazz.isArray());

                        sb.append(clazz.getName());

                        for (int j = count; j > 0; j--) {
                            sb.append("[]");
                        }
                    } else {
                        sb.append(clazz.getName());
                    }
                } else {
                    sb.append(args[i].toString());
                }
            }
            sb.append('>');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GradfParameterizedType that = (GradfParameterizedType) o;

        if (!raw.equals(that.raw)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(args, that.args)) {
            return false;
        }
        return Objects.equals(owner, that.owner);

    }

    @Override
    public int hashCode() {
        int result = raw.hashCode();
        result = 31 * result + Arrays.hashCode(args);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
