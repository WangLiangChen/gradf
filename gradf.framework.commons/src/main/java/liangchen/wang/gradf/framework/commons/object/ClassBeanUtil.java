package liangchen.wang.gradf.framework.commons.object;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang 2019/10/25 13:41
 */
public enum ClassBeanUtil {
    /**
     *
     */
    INSTANCE;
    private final Map<String, BeanCopier> cache = new ConcurrentHashMap<>();
    private final Map<Class, Class> primitiveMap = new HashMap<>();
    private final Collection<Class> supportedTypes = new HashSet<>();

    ClassBeanUtil() {
        primitiveMap.put(boolean.class, Boolean.class);
        primitiveMap.put(byte.class, Byte.class);
        primitiveMap.put(char.class, Character.class);
        primitiveMap.put(short.class, Short.class);
        primitiveMap.put(int.class, Integer.class);
        primitiveMap.put(long.class, Long.class);
        primitiveMap.put(float.class, Float.class);
        primitiveMap.put(double.class, Double.class);
        primitiveMap.put(String.class, String.class);

        supportedTypes.add(Boolean.class);
        supportedTypes.add(Byte.class);
        supportedTypes.add(Character.class);
        supportedTypes.add(Short.class);
        supportedTypes.add(Integer.class);
        supportedTypes.add(Long.class);
        supportedTypes.add(Float.class);
        supportedTypes.add(Double.class);
        supportedTypes.add(String.class);
    }

    public Class<?> forName(String typeName) {
        if (StringUtil.INSTANCE.isBlank(typeName)) {
            throw new InfoException("参数typeName不能为空");
        }
        if (typeName.equals("byte[]")) {
            typeName = "[B";
        }
        try {
            return Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new ErrorException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T cast(Object obj) {
        if (null == obj) {
            return null;
        }
        return (T) obj;
    }

    public <T> T copyProperties(Object source, Class<T> targetClass) {
        T target;
        try {
            target = targetClass.newInstance();
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        copyProperties(source, target);
        return target;
    }

    public void copyProperties(Object source, Object target) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        String key = String.format("%s_%s", sourceClass.getName(), targetClass.getName());
        BeanCopier beanCopier = cache.get(key);
        if (beanCopier == null) {
            synchronized (key.intern()) {
                beanCopier = cache.get(key);
                if (beanCopier == null) {
                    beanCopier = BeanCopier.create(sourceClass, targetClass, false);
                    cache.put(key, beanCopier);
                }
            }
        }
        beanCopier.copy(source, target, null);
    }

    public <T> Map<String, Object> bean2map(T bean) {
        Assert.INSTANCE.notNull(bean, "参数bean不能为null");
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, Object> map = new HashMap<>(beanMap.size());
        for (Object key : beanMap.keySet()) {
            map.put(String.valueOf(key), beanMap.get(key));
        }
        return map;
    }

    public <T> T map2bean(Map<String, Object> map, T bean) {
        Assert.INSTANCE.notNull(bean, "参数bean不能为null");
        Assert.INSTANCE.notNull(map, "参数map不能为null");
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    public <T> T map2bean(Map<String, Object> map, Class<T> beanClass) {
        Assert.INSTANCE.notNull(beanClass, "参数beanClass不能为null");
        try {
            T t = beanClass.getDeclaredConstructor().newInstance();
            t = map2bean(map, t);
            return t;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public String bean2xml(Object bean, String root) {
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("_-", "_")));
        xStream.alias(root, bean.getClass());
        String xml = xStream.toXML(bean);
        return xml;
    }

    public <T> T xml2bean(String xml, Class<T> cls, String root) {
        XStream xStream = new XStream(new DomDriver("utf-8"));
        xStream.alias(root, cls);
        T o = ClassBeanUtil.INSTANCE.cast(xStream.fromXML(xml));
        return o;
    }

    @SuppressWarnings("unchecked")
    public byte[] bean2ByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    public Object byteArray2Bean(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public Map<String, Object> dictionarySort(Map<String, Object> source) {
        Assert.INSTANCE.notNull(source, "参数source不能为空");
        Map<String, Object> result = source.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
        return result;
    }

    public Object deepClone(Object o) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

    public boolean isPrimitive(Object object) {
        return isPrimitive(object.getClass());
    }

    public boolean isPrimitive(Class<?> clazz) {
        if (clazz.isAssignableFrom(String.class)) {
            return true;
        }
        if (clazz.isPrimitive()) {
            return true;
        }
        try {
            return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T primitive2Wrapper(Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isAssignableFrom(type)) {
            return (T) value;
        }
        Class<T> wrapperType = primitiveMap.get(type);
        if (wrapperType.isAssignableFrom(Character.class)) {
            char[] c = value.toString().toCharArray();
            if (c == null || c.length == 0) {
                return null;
            } else {
                return (T) Character.valueOf(c[0]);
            }
        }
        try {
            Method m = wrapperType.getMethod("valueOf", String.class);
            int mods = m.getModifiers();
            if (Modifier.isStatic(mods) && Modifier.isPublic(mods)) {
                return (T) m.invoke(null, value.toString());
            } else {
                throw new ErrorException(String.format("Can not resolve value: %s to type: %s", value.toString(), type));
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    // 获取泛型类型
    public Type[] parameterizedType(Type type) {
        if (type == null || !(type instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types == null || types.length == 0) {
            return null;
        }
        return types;
    }

    // 获取泛型的第一个类型
    public Class<?> firstParameterizedType(Type type) {
        Type[] types = parameterizedType(type);
        if (types == null || types.length == 0) {
            return null;
        }
        return (Class<?>) types[0];
    }

    //获取泛型的原生类型
    public Class<?> parameterizedTypeRawType(Type type) {
        if (null == type) {
            return null;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class) parameterizedType.getRawType();
        }
        return (Class) type;
    }
}
