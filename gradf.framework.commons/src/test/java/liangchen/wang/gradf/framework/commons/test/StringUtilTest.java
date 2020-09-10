package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.junit.jupiter.api.Test;

/**
 * @author liangchen.wang 2020/9/10
 */
public class StringUtilTest {

    @Test
    public void testFormat(){
        String source = "My name is:{},I have {hands} hands";
        source = StringUtil.INSTANCE.format(source,"liangchen.wang",2);
        Assert.INSTANCE.isEquals(source,"My name is:liangchen.wang,I have 2 hands","fail");
    }
}
