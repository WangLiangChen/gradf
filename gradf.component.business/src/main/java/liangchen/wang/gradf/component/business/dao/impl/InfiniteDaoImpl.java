package liangchen.wang.gradf.component.business.dao.impl;

import liangchen.wang.gradf.component.business.dao.IInfiniteDao;
import liangchen.wang.gradf.component.business.dao.entity.Infinite;
import liangchen.wang.gradf.component.business.dao.query.InfiniteQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:27
 */
@Repository("Gradf_Business_DefaultInfiniteDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class InfiniteDaoImpl extends AbstractBaseDao<Infinite, InfiniteQuery> implements IInfiniteDao {
    @Override
    public void insertChild(Infinite infinite) {
        // 查询父级节点右值
        Long parent_id = infinite.getParent_id();
        InfiniteQuery query = InfiniteQuery.newInstance();
        query.setInfinite_id(parent_id);
        query.setStatus(Status.NORMAL.name());
        Infinite parent = one(query, "business_flag,infinite_grade,infinite_right");
        Assert.INSTANCE.notNull(parent,  "数据不存在或者状态错误");

        String business_flag = parent.getBusiness_flag();
        Byte infinite_grade = parent.getInfinite_grade();
        Short infinite_right = parent.getInfinite_right();
        // 第一步 腾位，左序大于当前节点右序的左右序号都自增2
        jdbcTemplate.update("update gradf_infinite set infinite_left=infinite_left+2,infinite_right=infinite_right+2 where business_flag=? and infinite_left>?", business_flag, infinite_right);
        // 第二步 入位 新节点的左序等于当前节点的右序，新节点的右序等于当前节点的右序+1
        infinite.setBusiness_flag(business_flag);
        infinite.setInfinite_left(infinite_right);
        infinite.setInfinite_right((short) (infinite_right + 1));
        infinite.setInfinite_grade((byte) (infinite_grade + 1));
        insert(infinite);
        // 第三步 父节点右序加2
        jdbcTemplate.update("update gradf_infinite set infinite_right=infinite_right+2 where infinite_id=?", parent_id);
    }

    @Override
    public void delete(Long infinite_id) {
        InfiniteQuery query = InfiniteQuery.newInstance();
        query.setInfinite_id(infinite_id);
        query.setStatusNotEquals(Status.DELETED.name());
        Infinite infinite = one(query, "infinite_left,infinite_right,business_flag");
        Assert.INSTANCE.notNull(infinite,  "数据不存在或状态错误");

        String business_flag = infinite.getBusiness_flag();
        Short infinite_right = infinite.getInfinite_right();
        Short infinite_left = infinite.getInfinite_left();
        int width = infinite_right - infinite_left + 1;
        // 第一步 删除节点及子孙节点
        jdbcTemplate.update("update gradf_infinite set status=?,infinite_left=0,infinite_right=0 where business_flag=? and infinite_left>=? and infinite_right<=?",
                Status.DELETED.name(), business_flag, infinite_left, infinite_right);
        // 第二步 修改左值
        jdbcTemplate.update("update gradf_infinite set infinite_left=infinite_left-? where business_flag=? and infinite_left>?", width, business_flag, infinite_right);
        // 第三步 修改右值
        jdbcTemplate.update("update gradf_infinite set infinite_right=infinite_right-? where business_flag=? and infinite_right>?", width, business_flag, infinite_right);
    }
}
