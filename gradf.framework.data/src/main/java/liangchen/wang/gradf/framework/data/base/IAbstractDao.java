package liangchen.wang.gradf.framework.data.base;


import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;
import java.util.Optional;

public interface IAbstractDao<E extends RootEntity> {

    boolean insert(E entity);

    int deleteByQuery(RootQuery query);

    int updateByQuery(E entity, RootQuery query);

    boolean exist(RootQuery query);

    int count(RootQuery query);

    E one(RootQuery query, String... returnFields);

    Optional<E> oneOptional(RootQuery query, String... returnFields);

    List<E> list(RootQuery query, String... returnFields);

    PaginationResult<E> pagination(RootQuery query, String... returnFields);
}
