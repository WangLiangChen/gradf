package liangchen.wang.gradf.framework.commons.utils;

import com.google.common.collect.Lists;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author LiangChen.Wang
 */
public enum CollectionUtil {
    /**
     *
     */
    INSTANCE;

    public boolean isEmpty(byte[] bytes) {
        return (null == bytes || bytes.length == 0);
    }

    public <T> boolean isEmpty(T[] array) {
        return (null == array || array.length == 0);
    }

    public <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public boolean isNotEmpty(byte[] bytes) {
        return !isEmpty(bytes);
    }

    public <T> boolean isEmpty(Collection<T> collection) {
        return (null == collection || collection.isEmpty());
    }

    public <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public <T> boolean isEmpty(Iterator<T> iterator) {
        return (null == iterator || !iterator.hasNext());
    }

    public <T> boolean isNotEmpty(Iterator<T> iterator) {
        return !isEmpty(iterator);
    }

    public <K, V> boolean isEmpty(Map<K, V> map) {
        return (null == map || map.isEmpty());
    }

    public <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    public <T> boolean contains(T[] array, T value) {
        if (null == array || array.length == 0) {
            return false;
        }
        return Arrays.stream(array).anyMatch(value::equals);
    }

    public <T> boolean notContains(T[] array, T value) {
        return !contains(array, value);
    }


    public <T> List<T> array2List(T[] array) {
        if (isEmpty(array)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(Arrays.asList(array));
    }

    public <T> Set<T> array2Set(T[] array) {
        if (isEmpty(array)) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(array));
    }

    public <S, T> List<T> copyList(Collection<S> sources, Class<T> targetClass) {
        return copyList(sources, targetClass, null);
    }

    public <S, T> List<T> copyList(Collection<S> sources, Class<T> targetClass, Consumer<T> consumer) {
        Assert.INSTANCE.notNull(targetClass, "参数targetClass不能为空");
        if (isEmpty(sources)) {
            return Collections.emptyList();
        }
        List<T> targets = new ArrayList<>(sources.size());
        sources.forEach(e -> {
            T target = ClassBeanUtil.INSTANCE.copyProperties(e, targetClass);
            if (null != consumer) {
                consumer.accept(target);
            }
            targets.add(target);
        });
        return targets;
    }

    public <T> List<List<T>> slice(List<T> datas, int sliceNumber) {
        if (isEmpty(datas)) {
            return Collections.emptyList();
        }
        Assert.INSTANCE.isTrue(sliceNumber > 0, "分片数必须大于0");
        int size = datas.size();
        //计算每片数量
        int elementNumber = size / sliceNumber;
        if (size % sliceNumber > 0) {
            elementNumber++;
        }
        List<List<T>> partition = Lists.partition(datas, elementNumber);
        size = partition.size();
        if (sliceNumber == size) {
            return partition;
        }
        List<List<T>> result = new ArrayList<>(partition);
        size = sliceNumber - size;
        for (int i = 0; i < size; i++) {
            result.add(Collections.emptyList());
        }
        return result;
    }

    public <T> List<T> iterator2List(Iterator<T> iterator) {
        if (isEmpty(iterator)) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public <T> void slice(Stream<T> stream, int sliceSize, Consumer<List<T>> consumer) {
        AtomicInteger atomicIndex = new AtomicInteger();
        ArrayList<T>[] container = new ArrayList[1];
        stream.forEach(e -> {
            int index = atomicIndex.getAndIncrement();
            if (0 == index) {
                container[0] = new ArrayList<>();
            }
            container[0].add(e);
            if (index + 2 > sliceSize) {
                atomicIndex.set(0);
                consumer.accept(container[0]);
            }
        });
        //输出剩余数据
        if (container[0].size() < sliceSize) {
            consumer.accept(container[0]);
        }
    }
}
