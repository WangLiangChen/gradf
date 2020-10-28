package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.utils.CommandUtil;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author liangchen.wang 2020/9/10
 */
public class CommandTest {

    @Test
    public void testPing() {
        CommandUtil.INSTANCE.execute(line->{
            System.out.println("===>"+line);
        },"ping","127.0.0.1");
    }


    @Test
    public void testBgpdump() {
        Path sourcePath = Paths.get("/root/RouteDatas/routeviews/rib.20011026.1648.bz2");
        CommandUtil.INSTANCE.execute(line->{
            System.out.println("===>"+line);
        },"b","127.0.0.1");
    }

    @Test
    public void testCopy() {
        Path sourcePath = Paths.get("D:\\RIPE\\RRC01\\2020.10\\updates\\updates.20201022.0545.gz");
        Path targetPath = sourcePath.resolveSibling(sourcePath.getFileName() + ".txt");
        CommandUtil.INSTANCE.execute("cmd", "/c copy " + sourcePath + " " + targetPath);
    }

    @Test
    public void testLinux() {
        Path sourcePath = Paths.get("/root/RouteDatas/routeviews/rib.20011026.1648.bz2");
        Path targetPath = sourcePath.resolveSibling(sourcePath.getFileName() + ".txt");
        String result = CommandUtil.INSTANCE.executeWithResult("bgpdump", "-m", "-q", "-O", targetPath.toString(), sourcePath.toString());
        System.out.println(result);
    }

    @Test
    public void testDate() {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(1004140086, 0, ZoneOffset.UTC);
        System.out.println(localDateTime);
    }


}
