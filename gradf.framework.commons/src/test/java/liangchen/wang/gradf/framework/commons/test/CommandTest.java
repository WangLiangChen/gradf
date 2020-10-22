package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.utils.CommandUtil;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author liangchen.wang 2020/9/10
 */
public class CommandTest {

    @Test
    public void testPing() throws UnsupportedEncodingException {
        String ping = CommandUtil.INSTANCE.executeWithResult("ping", "127.0.0.1");
        System.out.println(ping);
    }

    @Test
    public void testCopy() {
        Path sourcePath = Paths.get("D:\\RIPE\\RRC01\\2020.10\\updates\\updates.20201022.0545.gz");
        Path targetPath = sourcePath.resolveSibling(sourcePath.getFileName() + ".txt");
        CommandUtil.INSTANCE.execute("cmd", "/c copy " + sourcePath + " " + targetPath);
    }


}
