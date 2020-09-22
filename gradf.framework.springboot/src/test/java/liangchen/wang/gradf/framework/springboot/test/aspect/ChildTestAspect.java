package liangchen.wang.gradf.framework.springboot.test.aspect;

import org.springframework.stereotype.Component;

/**
 * @author LiangChen.Wang 2020/9/21
 */
//@TestAspectAnnotation
@Component
public class ChildTestAspect extends ParentTestAspect {
    public void childMethod() {

    }
}
