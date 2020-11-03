package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public enum CompressUtil {
    //
    INSTANCE;

    public void gzDecompress(InputStream inputStream, Consumer<String> consumer) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            Stream<String> lines = bufferedReader.lines();
            lines.forEach(line -> consumer.accept(line));
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    public String gzDecompress(InputStream inputStream) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.lines().collect(Collectors.joining(Symbol.LINE_SEPARATOR.getSymbol()));
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }
}
