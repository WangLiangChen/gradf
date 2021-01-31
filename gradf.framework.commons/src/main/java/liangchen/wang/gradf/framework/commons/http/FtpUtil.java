package liangchen.wang.gradf.framework.commons.http;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.utils.ThreadPoolUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author LiangChen.Wang
 */
public enum FtpUtil {
    //
    INSTANCE;

    public void download(String url, NetResponse<InputStream> netResponse) {
        URIResolver uriResolver = new URIResolver();
        uriResolver.resolve(url);
        download(uriResolver, netResponse);
    }

    public void download(URIResolver uriResolver, NetResponse<InputStream> netResponse) {
        ThreadPoolUtil.INSTANCE.getExecutor().execute(() -> {
            FTPClient ftpClient = new FTPClient();
            ftpClient.setConnectTimeout(60000);
            try {
                ftpClient.connect(uriResolver.getHost(), uriResolver.getPort());
                ftpClient.login(uriResolver.getUsername(), uriResolver.getPassword());
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                int replyCode = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    ftpClient.disconnect();
                    netResponse.onFailure(new ErrorException("replyCode:{}", replyCode));
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
                netResponse.onResponse(inputStream);
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
        });
    }

    public FTPFile[] listFiles(String url) {
        URIResolver uriResolver = new URIResolver();
        uriResolver.resolve(url);
        return listFiles(uriResolver);
    }

    public FTPFile[] listFiles(URIResolver uriResolver) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(60000);
        try {
            ftpClient.connect(uriResolver.getHost(), uriResolver.getPort());
            ftpClient.login(uriResolver.getUsername(), uriResolver.getPassword());
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                return new FTPFile[0];
            }
            String path = uriResolver.getPath();
            if (StringUtil.INSTANCE.isNotBlank(path)) {
                ftpClient.changeWorkingDirectory(path);
            }
            ftpClient.enterLocalPassiveMode();
            FTPFile[] ftpFiles = ftpClient.listFiles();
            return ftpFiles;
        } catch (IOException e) {
            throw new ErrorException(e);
        } finally {
            try {
                ftpClient.logout();
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                return new FTPFile[0];
            }
        }
    }
}
