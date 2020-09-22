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
        return JsonUtil.INSTANCE.parseList(this, clazz);
    }

    public String toJSONString() {
        return JsonUtil.INSTANCE.toJsonString(this.list);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(Object e) {
        return list.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
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

    @Override
    public void add(int index, Object element) {
        list.add(index, element);
    }

    @Override
    public Object remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public Object get(int index) {
        return list.get(index);
    }

    @Override
    public Object clone() {
        return new JsonList(new ArrayList<Object>(list));
    }

    @Override
    public boolean equals(Object obj) {
        return this.list.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

}
