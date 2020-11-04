package liangchen.wang.gradf.framework.commons.http;

import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author LiangChen.Wang
 */
public enum FtpUtil {
    //
    INSTANCE;

    public void download(String url, NetResponse netResponse) {
        URIResolver uriResolver = new URIResolver();
        uriResolver.resolve(url);
        download(uriResolver, netResponse);
    }

    public void download(URIResolver uriResolver, NetResponse netResponse) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(60000);
        try {
            ftpClient.connect(uriResolver.getHost(), uriResolver.getPort());
            ftpClient.login(uriResolver.getUsername(), uriResolver.getPassword());
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                return;
            }
            String path = uriResolver.getPath();
            if (StringUtil.INSTANCE.isNotBlank(path)) {
                ftpClient.changeWorkingDirectory(path);
            }
            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            netResponse.onFailure(e);
            return;
        }
        InputStream inputStream = null;
        try {
            //谁用谁关闭
            inputStream = ftpClient.retrieveFileStream(uriResolver.getFileName());
        } catch (IOException e) {
            netResponse.onFailure(e);
        } finally {
            try {
                ftpClient.logout();
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                netResponse.onFailure(e);
                return;
            }
        }
        // 提出来是为了不处理回调中的异常
        if (null != inputStream) {
            netResponse.onResponse(inputStream);
        }
    }
}
