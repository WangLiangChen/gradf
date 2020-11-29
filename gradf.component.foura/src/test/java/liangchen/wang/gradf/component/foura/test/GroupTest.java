package liangchen.wang.gradf.component.foura.test;

import liangchen.wang.gradf.component.foura.dao.IGroupDao;
import liangchen.wang.gradf.component.foura.dao.entity.Group;
import liangchen.wang.gradf.component.foura.dao.query.GroupQuery;
import liangchen.wang.gradf.component.foura.manager.IGroupManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.GroupParameterDomain;
import liangchen.wang.gradf.framework.cache.annotation.EnableGradfCaching;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import liangchen.wang.gradf.framework.data.annotation.EnableJdbc;
import liangchen.wang.gradf.framework.data.enumeration.DataMode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableJdbc
@EnableGradfCaching
public class GroupTest {
    @Inject
    private IGroupManager manager;
    @Inject
    private Executor executor;
    @Inject
    private IGroupDao groupDao;

    @Test
    public void testInsert() {
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                GroupParameterDomain parameterDomain = GroupParameterDomain.newInstance();
                parameterDomain.setGroup_key("unit_test");
                parameterDomain.setGroup_text("单元测试");
                parameterDomain.setData_mode(DataMode.N.getValue());
                manager.insert(parameterDomain);
            });
        }
        ConcurrentUtil.INSTANCE.threadSleep(5, TimeUnit.SECONDS);
    }

    @Test
    public void one() {
        Group one = groupDao.one(GroupQuery.newInstance(3154522190351695872L));
        System.out.println(one);
    }

    @Test
    public void list() {
        List<Group> list = groupDao.list(GroupQuery.newInstance(3154522190351695872L));
        System.out.println(list);
    }

}
