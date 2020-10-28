package liangchen.wang.gradf.framework.commons.test;

import liangchen.wang.gradf.framework.commons.utils.CommandUtil;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
        byte[] buffer = new byte[8192];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        CommandUtil.INSTANCE.executeOut2In(inputStream, "ping", "127.0.0.1");
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBgpdump() {
        byte[] buffer = new byte[8192];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        Path sourcePath = Paths.get("/root/RouteDatas/routeviews/rib.20011026.1648.bz2");
        CommandUtil.INSTANCE.executeOut2In(inputStream, "bgpdump", "-M", "-q", sourcePath.toString());
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
