package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import org.apache.commons.exec.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public enum CommandUtil {
    //
    INSTANCE;

    public void execute(String command, String... args) {
        execute(null, 0, null, command, args);
    }

    public void execute(long timeout, TimeUnit timeUnit, String command, String... args) {
        execute(null, timeout, timeUnit, command, args);
    }

    public void executeWithConsumer(Consumer<String> consumer, String command, String... args) {
        executeWithConsumer(consumer, 0, null, command, args);
    }

    public void executeWithConsumer(Consumer<String> consumer, long timeout, TimeUnit timeUnit, String command, String... args) {
        PumpStreamHandler streamHandler = new PumpStreamHandler(new LogOutputStream() {
            @Override
            protected void processLine(String line, int i) {
                consumer.accept(line);
            }
        });
        execute(streamHandler, timeout, timeUnit, command, args);
    }

    private void execute(ExecuteStreamHandler streamHandler, long timeout, TimeUnit timeUnit, String command, String... args) {
        CommandLine cmdLine = findCommanLine(command, args);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValues(null);
        if (null == streamHandler) {
            executor.setStreamHandler(new PumpStreamHandler(null, null, null));
        } else {
            executor.setStreamHandler(streamHandler);
        }
        if (timeout > 0 && null != timeUnit) {
            ExecuteWatchdog watchdog = new ExecuteWatchdog(timeUnit.toMillis(timeout));
            executor.setWatchdog(watchdog);
        }
        ResultHandler resultHandler = new ResultHandler();
        try {
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    private CommandLine findCommanLine(String command, String... args) {
        //第一个空格之后的所有参数都为参数
        CommandLine cmdLine = new CommandLine(command);
        cmdLine.addArguments(args);
        return cmdLine;
    }

    class ResultHandler extends DefaultExecuteResultHandler {

    }

}
