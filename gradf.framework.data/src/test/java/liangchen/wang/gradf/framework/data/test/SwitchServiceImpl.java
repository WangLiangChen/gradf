package liangchen.wang.gradf.framework.data.test;

import liangchen.wang.gradf.framework.data.annotation.DataSourceSwitchable;
import liangchen.wang.gradf.framework.data.annotation.SwitchDataSource;
import org.springframework.stereotype.Component;

/**
 * @author LiangChen.Wang 2021/5/31
 */
@Component
@DataSourceSwitchable
public class SwitchServiceImpl implements ISwitchService{
    @Override
    @SwitchDataSource("abc")
    public void doA(){
        System.out.println();
    }

}
