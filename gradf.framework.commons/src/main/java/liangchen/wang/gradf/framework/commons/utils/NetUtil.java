package liangchen.wang.gradf.framework.commons.utils;


import liangchen.wang.gradf.framework.commons.exception.ErrorException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author LiangChen.Wang
 */
public enum NetUtil {
    /**
     * instance
     */
    INSTANCE;
    private final InetAddress inetAddress;
    private final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    private final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

    NetUtil() {
        try {
            inetAddress = getLocalInetAddress();
        } catch (SocketException e) {
            throw new ErrorException(e);
        }
    }

    public String getLocalHostAddress() {
        return inetAddress.getHostAddress();
    }

    public String getLocalHostName() {
        return inetAddress.getHostName();
    }

    public URI toURI(String string, String... more) {
        string = string.replaceAll("\\\\", "/");
        if (CollectionUtil.INSTANCE.isNotEmpty(more)) {
            String spec = String.join("/", more);
            spec = spec.replaceAll("\\\\", "/");
            spec = spec.replaceAll("//", "/");
            if (string.endsWith("/")) {
                string = String.format("%s%s", string, spec);
            } else {
                string = String.format("%s/%s", string, spec);
            }
        }
        try {
            Path path = Paths.get(string);
            return path.toUri();
        } catch (InvalidPathException e) {
            return URI.create(string);
        }
    }

    public URL toURL(String string, String... more) {
        URI uri = toURI(string, more);
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new ErrorException(e);
        }
    }

    public URL concatURL(URL url, String... more) {
        return toURL(url.toString(), more);
    }

    public boolean isAvailableURL(URL url) {
        try {
            url.openStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReachable(String host, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(host);
            return address.isReachable(timeout);
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isConnectable(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String urlEncode(String url, Charset charset) {
        try {
            return URLEncoder.encode(url, charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new ErrorException(e);
        }
    }

    public String urlDecode(String url, Charset charset) {
        try {
            return URLDecoder.decode(url, charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new ErrorException(e);
        }
    }

    public String longToIpV4(long longIp) {
        int octet3 = (int) ((longIp >> 24) % 256);
        int octet2 = (int) ((longIp >> 16) % 256);
        int octet1 = (int) ((longIp >> 8) % 256);
        int octet0 = (int) ((longIp) % 256);

        return octet3 + "" + octet2 + "" + octet1 + "" + octet0;
    }

    public long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16) + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }

    public boolean isIPv4Private(String ip) {
        long longIp = ipV4ToLong(ip);
        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255")) || (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
                || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
    }

    public boolean isIPv4Valid(String ip) {
        return pattern.matcher(ip).matches();
    }

    public String getIpFromRequest(HttpServletRequest request) {
        String ip;
        ip = request.getHeader("x-forwarded-for");
        if (StringUtil.INSTANCE.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.INSTANCE.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.INSTANCE.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (!StringUtil.INSTANCE.isBlank(ip)) {
            StringTokenizer tokenizer = new StringTokenizer(ip, ",");
            while (tokenizer.hasMoreTokens()) {
                ip = tokenizer.nextToken().trim();
                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
                    break;
                }
            }
        }

        if (StringUtil.INSTANCE.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!isIPv4Valid(ip) || isIPv4Private(ip)) {
            return "127.0.0.1";
        }
        if (StringUtil.INSTANCE.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    public Map<String, String> queryString2Map(String queryString) {
        if (StringUtil.INSTANCE.isBlank(queryString)) {
            return Collections.emptyMap();
        }
        String[] kvs = queryString.split("&");
        String[] kv;
        Map<String, String> result = new HashMap<>(kvs.length);
        for (int i = 0; i < kvs.length; i++) {
            kv = kvs[i].split("=");
            if (kv.length == 1) {
                result.put(kv[0], "");
            } else {
                result.put(kv[0], kv[1]);
            }
        }
        return result;
    }

    private InetAddress getLocalInetAddress() throws SocketException {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress = null;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback()) {
                    continue;
                }
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress() && !inetAddress.isAnyLocalAddress()) {
                        break;
                    }
                }
            }
            return inetAddress;
        }
    }
}