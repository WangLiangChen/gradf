package liangchen.wang.gradf.framework.springboot.utils;

import org.springframework.util.StopWatch;

/**
 * @author LiangChen.Wang
 */
public enum StopWatchUtil {
    //
    INSTANCE;

    public StopWatch newAndStart(String taskName) {
        StopWatch stopWatch = newInstance();
        stopWatch.start(taskName);
        return stopWatch;
    }

    public StopWatch newInstance() {
        return new StopWatch();
    }
}
