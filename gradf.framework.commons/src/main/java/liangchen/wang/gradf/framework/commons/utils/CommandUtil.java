package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public enum CommandUtil {
    //
    INSTANCE;
    private final int DEFAULT_BUFFER_SIZE = 8192;

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

    public void execute(Consumer<String> consumer, String command, String... args) {
        PumpStreamHandler streamHandler = new PumpStreamHandler(new LogOutputStream() {
            @Override
            protected void processLine(String line, int i) {
                consumer.accept(line);
            }
        });
        executeWithStreamHandler(streamHandler,command,args);
    }

    public void executeWithStreamHandler(ExecuteStreamHandler streamHandler, String command, String... args) {
        CommandLine cmdLine = findCommanLine(command, args);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValues(null);
        executor.setStreamHandler(streamHandler);
        try {
            executor.execute(cmdLine);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }


    public String executeWithResult(String command, String... args) {
        CommandLine cmdLine = findCommanLine(command, args);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        executeWithStreamHandler(streamHandler, command, args);
        try {
            String out = outputStream.toString("gbk");
            return out;
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
