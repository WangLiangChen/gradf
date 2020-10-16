package liangchen.wang.gradf.component.foura.test;

import liangchen.wang.gradf.component.foura.manager.IGroupManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.GroupParameterDomain;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import liangchen.wang.gradf.framework.data.annotation.EnableJdbc;
import liangchen.wang.gradf.framework.data.enumeration.DataMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJdbc
public class GroupTest {
    @Inject
    private IGroupManager manager;
    @Inject
    private Executor executor;

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

}
