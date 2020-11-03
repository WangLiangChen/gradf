package liangchen.wang.gradf.framework.commons.file;

import liangchen.wang.gradf.framework.commons.bytes.ByteOutputStream;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

/**
 * @author LiangChen.Wang 2019/10/14 17:43
 */
public enum FileUtil {
    /**
     *
     */
    INSTANCE;
    private final int BUFFER = 2048;

    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public void createFileOrDirectory(String first, String... more) throws IOException {
        Path path = Paths.get(first, more);
        if (Files.exists(path)) {
            return;
        }
        if (Files.isDirectory(path)) {
            Files.createDirectory(path);
            return;
        }
        Files.createFile(path);
    }

    public ByteOutputStream decompressBZ2(InputStream inputStream) {
        ByteOutputStream outputStream = new ByteOutputStream();
        try (InputStream bz2InputStream = inputStream instanceof BZip2CompressorInputStream ? inputStream : new BZip2CompressorInputStream(inputStream);) {
            outputStream.write(bz2InputStream);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
        return outputStream;
    }

    public ByteOutputStream decompressGz(InputStream inputStream) {
        ByteOutputStream outputStream = new ByteOutputStream();
        try (InputStream gzInputStream = inputStream instanceof GZIPInputStream ? inputStream : new GZIPInputStream(inputStream);) {
            outputStream.write(gzInputStream);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
        return outputStream;
    }

    public Map<String, ByteOutputStream> decompressTar(InputStream inputStream) {
        Map<String, ByteOutputStream> map = new HashMap<>();
        try (TarArchiveInputStream tarInputStream = inputStream instanceof TarArchiveInputStream ? (TarArchiveInputStream) inputStream : new TarArchiveInputStream(inputStream);) {
            TarArchiveEntry tarEntry;
            byte[] buffer = new byte[4096];
            while ((tarEntry = tarInputStream.getNextTarEntry()) != null) {
                // TODO
            }
        } catch (IOException e) {
            throw new ErrorException(e);
        }
        return map;
    }


}
