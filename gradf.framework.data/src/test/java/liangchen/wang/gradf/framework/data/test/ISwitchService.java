package liangchen.wang.gradf.framework.data.test;

import javax.transaction.Transactional;

/**
 * @author LiangChen.Wang 2021/5/31
 */
public interface ISwitchService {

    @Transactional
    void testTransactional();
}
