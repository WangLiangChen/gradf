package liangchen.wang.gradf.framework.commons.http;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import okhttp3.internal.Util;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public enum FtpUtil {
    //
    INSTANCE;
    private final ExecutorService executorService;

    FtpUtil() {
        // 核心线程数0,最大线程数Integer.MAX_VALUE,空闲线程超时时间60 SECONDS , 线程等待队列SynchronousQueue(容量为0的队列)
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), Util.threadFactory("FtpClientThread", false));
    }

    public void download(String url, NetResponse netResponse) {
        URIResolver uriResolver = new URIResolver();
        uriResolver.resolve(url);
        download(uriResolver, netResponse);
    }

    public void download(URIResolver uriResolver, NetResponse netResponse) {
        executorService.execute(() -> {
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
            return new FTPFile[0];
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
