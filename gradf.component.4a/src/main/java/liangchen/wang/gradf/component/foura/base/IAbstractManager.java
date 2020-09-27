package liangchen.wang.gradf.component.foura.base;

import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.base.RootQuery;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author LiangChen.Wang 2019/11/21 9:56
 */
public interface IAbstractManager<R extends ResultDomain> {
    boolean insert(ParameterDomain parameter);

    int deleteByQuery(RootQuery query);

    int updateByQuery(ParameterDomain parameter, RootQuery query);

    boolean exist(RootQuery query);

    int count(RootQuery query);

    R one(RootQuery query, String... returnFields);

    R one(RootQuery query, Consumer<R> consumer, String... returnFields);

    List<R> list(RootQuery query, String... returnFields);

    List<R> list(RootQuery query, Consumer<R> consumer, String... returnFields);

    PaginationResult<R> pagination(RootQuery query, String... returnFields);

    PaginationResult<R> pagination(RootQuery query, Consumer<R> consumer, String... returnFields);

}
