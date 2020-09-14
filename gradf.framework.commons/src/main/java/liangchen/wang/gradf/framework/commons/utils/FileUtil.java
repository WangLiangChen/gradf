package liangchen.wang.gradf.framework.commons.utils;

import java.util.Optional;

/**
 * @author LiangChen.Wang 2019/10/14 17:43
 */
public enum FileUtil {
    /**
     *
     */
    INSTANCE;

    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
