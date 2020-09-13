package liangchen.wang.gradf.framework.commons.json;

import java.io.Serializable;
import java.util.*;

/**
 * @author WangLiangChen
 */
public class JsonList extends Json implements List<Object>, Cloneable, RandomAccess, Serializable {
    private final List<Object> list;

    public JsonList() {
        this.list = new ArrayList<>();
    }

    public JsonList(List<Object> list) {
        if (list == null) {
            throw new IllegalArgumentException("list is null.");
        }
        this.list = list;
    }

    @SuppressWarnings("unchecked")
    public JsonMap getJSONMap(int index) {
        Object value = list.get(index);
        if (null == value) {
            return new JsonMap();
        }

        if (value instanceof JsonMap) {
            return (JsonMap) value;
        }

        if (value instanceof Map) {
            return new JsonMap((Map<String, Object>) value);
        }
        throw new RuntimeException("object is not a JSONMap");
    }

    @SuppressWarnings("unchecked")
    public JsonList getJSONList(int index) {
        Object value = list.get(index);
        if (null == value) {
            return new JsonList();
        }
        if (value instanceof JsonList) {
            return (JsonList) value;
        }
        if (value instanceof List) {
            return new JsonList((List<Object>) value);
        }
        throw new RuntimeException("object is not a JSONList");
    }

    public String getString(int index) {
        Object value = get(index);
        if (null == value) {
            return null;
        }
        return String.valueOf(value);
    }

    public Integer getInteger(int index) {
        String value = getString(index);
        if (null == value) {
            return null;
        }
        return Integer.valueOf(value);
    }

    public int getIntValue(int index) {
        Integer value = getInteger(index);
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    public <T> List<T> toJavaList(Class<T> clazz) {
        List<T> list = new ArrayList<>(this.list.size());

        for (Object e : this.list) {
            String jsonString = JsonUtil.INSTANCE.toJSONString(e);
            T t = JsonUtil.INSTANCE.parseObject(jsonString, clazz);
            list.add(t);
        }
        return list;
    }

    public String toJSONString() {
        return JsonUtil.INSTANCE.toJSONString(this.list);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<Object> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    public boolean add(Object e) {
        return list.add(e);
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    public boolean addAll(Collection<? extends Object> c) {
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Object> c) {
        return list.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    public void clear() {
        list.clear();
    }

    public Object set(int index, Object element) {
        if (index == -1) {
            list.add(element);
            return null;
        }

        if (list.size() <= index) {
            for (int i = list.size(); i < index; ++i) {
                list.add(null);
            }
            list.add(element);
            return null;
        }

        return list.set(index, element);
    }

    public void add(int index, Object element) {
        list.add(index, element);
    }

    public Object remove(int index) {
        return list.remove(index);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    public ListIterator<Object> listIterator(int index) {
        return list.listIterator(index);
    }

    public List<Object> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public Object get(int index) {
        return list.get(index);
    }

    @Override
    public Object clone() {
        return new JsonList(new ArrayList<Object>(list));
    }

    public boolean equals(Object obj) {
        return this.list.equals(obj);
    }

    public int hashCode() {
        return this.list.hashCode();
    }

}
