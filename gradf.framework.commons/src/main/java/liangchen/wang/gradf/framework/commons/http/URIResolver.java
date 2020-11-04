package liangchen.wang.gradf.framework.commons.http;

import java.net.URI;

public class URIResolver {
    private String host;
    private int port;
    private String username = "anonymous";
    private String password = "";
    private String path;
    private String fileName = "";


    public void resolve(String url) {
        URI uri = URI.create(url);
        host = uri.getHost();
        port = uri.getPort();
        port = port == -1 ? 21 : port;
        path = uri.getPath();
        int index = path.lastIndexOf('/');
        if (index != -1) {
            fileName = path.substring(index + 1);
            path = path.substring(0, index);
        }
        String userInfo = uri.getUserInfo();
        if (null != userInfo) {
            index = userInfo.indexOf(':');
            if (index != -1) {
                username = userInfo.substring(index + 1);
                password = userInfo.substring(0, index);
            }
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }
}
