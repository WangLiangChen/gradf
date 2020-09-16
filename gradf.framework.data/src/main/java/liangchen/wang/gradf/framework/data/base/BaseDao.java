package liangchen.wang.gradf.framework.data.base;



import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;
import java.util.Optional;

/**
 * @author .LiangChen.Wang
 */
public class BaseDao extends AbstractDao {


    @Override
    public boolean insert(RootEntity entity) {
        return false;
    }

    @Override
    public int deleteByQuery(RootQuery query) {
        return 0;
    }

    @Override
    public int updateByQuery(RootEntity entity, RootQuery query) {
        return 0;
    }

    @Override
    public boolean exist(RootQuery query) {
        return false;
    }

    @Override
    public int count(RootQuery query) {
        return 0;
    }

    @Override
    public RootEntity one(RootQuery query, String... returnFields) {
        return null;
    }

    @Override
    public Optional<?> oneOptional(RootQuery query, String... returnFields) {
        return Optional.empty();
    }

    @Override
    public List list(RootQuery query, String... returnFields) {
        return null;
    }

    @Override
    public PaginationResult pagination(RootQuery query, String... returnFields) {
        return null;
    }
}
