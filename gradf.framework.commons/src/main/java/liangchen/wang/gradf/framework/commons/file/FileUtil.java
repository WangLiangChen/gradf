package liangchen.wang.gradf.framework.commons.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author LiangChen.Wang 2019/10/14 17:43
 */
public enum FileUtil {
    /**
     *
     */
    INSTANCE;
    private final int BUFFER = 2048;

    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public void createFileOrDirectory(String first, String... more) throws IOException {
        Path path = Paths.get(first, more);
        if (Files.exists(path)) {
            return;
        }
        if (Files.isDirectory(path)) {
            Files.createDirectory(path);
            return;
        }
        Files.createFile(path);
    }

}
