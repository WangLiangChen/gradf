package liangchen.wang.gradf.framework.commons.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author LiangChen.Wang
 */
public enum HostUtil {
    /**
     *
     */
    INSTANCE;

    public String hostName() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            return "n/a";
        }
    }

    public String osName() {
        return System.getProperty("os.name");
    }
}
