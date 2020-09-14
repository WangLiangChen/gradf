package liangchen.wang.gradf.framework.commons.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import liangchen.wang.gradf.framework.commons.bytes.ByteOutputStream;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author LiangChen.Wang 2019/12/9 18:14
 */
public enum QRCodeUtil {
    /**
     *
     */
    INSTANCE;
    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "png";

    public String createBase64(String content, Short size) {
        ByteOutputStream outputStream = createImageOutputStream(content, size);
        String base64 = Base64.getEncoder().encodeToString(outputStream.getBytes());
        base64 = String.format("data:image/%s;base64,%s", FORMAT_NAME, base64);
        return base64;
    }

    public ByteOutputStream createImageOutputStream(String content, Short size) {
        BufferedImage image = createImage(content, size);
        ByteOutputStream outputStream = new ByteOutputStream();
        try {
            ImageIO.write(image, FORMAT_NAME, outputStream);
            return outputStream;
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    public BufferedImage createImage(String content, Short size) {
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        } catch (WriterException e) {
            throw new ErrorException(e);
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }
}
