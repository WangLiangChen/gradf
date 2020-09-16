package liangchen.wang.gradf.framework.web.initializer;


import liangchen.wang.gradf.framework.commons.utils.Printer;

/**
 * @author LiangChen.Wang
 */
public abstract class SpringBootServletInitializer extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer {
    public SpringBootServletInitializer() {
        super();
        Printer.INSTANCE.prettyPrint("setRegisterErrorPageFilter=false");
        this.setRegisterErrorPageFilter(false);
    }
}
