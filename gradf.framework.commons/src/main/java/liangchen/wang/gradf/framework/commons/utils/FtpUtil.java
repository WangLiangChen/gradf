package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author LiangChen.Wang
 */
public enum FtpUtil {
    //
    INSTANCE;

    public void download(String host, int port, String path, String fileName, Consumer<InputStream> consumer) {
        download(host, port, null, null, path, fileName, consumer);
    }

    public void download(String host, int port, String username, String password, String path, String fileName, Consumer<InputStream> consumer) {
        FTPClient ftpClient = new FTPClient();
        InputStream inputStream = null;
        try {
            ftpClient.connect(host, port);
            if (null != username && null != password) {
                ftpClient.login(username, password);
            } else {
                ftpClient.login("anonymous", "");
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                return;
            }
            ftpClient.changeWorkingDirectory(path);
            ftpClient.enterLocalPassiveMode();
            inputStream = ftpClient.retrieveFileStream(fileName);
            consumer.accept(inputStream);
            ftpClient.logout();
        } catch (IOException e) {
            throw new ErrorException(e);
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                throw new ErrorException(e);
            }
        }

    }
}
