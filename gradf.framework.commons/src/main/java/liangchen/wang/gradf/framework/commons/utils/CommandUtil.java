package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public enum CommandUtil {
    //
    INSTANCE;

    public void execute(String command, String... args) {
        CommandLine cmdLine = findCommanLine(command, args);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setStreamHandler(new PumpStreamHandler(null, null, null));
        /*ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
        executor.setWatchdog(watchdog);*/
        DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
        try {
            executor.execute(cmdLine, handler);
            //命令执行返回前一直阻塞
            handler.waitFor();
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public String executeWithResult(String command, String... args) {
        CommandLine cmdLine = findCommanLine(command, args);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValues(null);
        executor.setStreamHandler(streamHandler);
        try {
            executor.execute(cmdLine);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
        try {
            String out = outputStream.toString("gbk");
            String error = errorStream.toString("gbk");
            return out + error;
        } catch (UnsupportedEncodingException e) {
            throw new ErrorException(e);
        }
    }

    private CommandLine findCommanLine(String command, String... args) {
        //第一个空格之后的所有参数都为参数
        CommandLine cmdLine = new CommandLine(command);
        cmdLine.addArguments(args);
        return cmdLine;
    }
}
