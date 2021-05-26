package liangchen.wang.gradf.component.foura.runner;

import liangchen.wang.gradf.component.foura.manager.IInitializationManager;
import liangchen.wang.gradf.framework.springboot.context.BeanLoader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author LiangChen.Wang 2021/5/26
 */
public class FouraApplicationRunner implements ApplicationRunner {
    private final IInitializationManager initializationManager;

    @Inject
    public FouraApplicationRunner(@Named("Gradf_Foura_DefaultInitializationManager") IInitializationManager initializationManager) {
        this.initializationManager = initializationManager;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化数据
        initializationManager.initFouraData();
        // 初始化权限
        initializationManager.initAuth();
    }
}
