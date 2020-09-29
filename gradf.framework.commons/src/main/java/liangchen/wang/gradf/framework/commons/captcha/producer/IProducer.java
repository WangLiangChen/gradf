package liangchen.wang.gradf.framework.commons.captcha.producer;


import liangchen.wang.gradf.framework.commons.bytes.ByteOutputStream;

import java.awt.image.BufferedImage;

/**
 * @author LiangChen.Wang 2019/7/3 15:42
 */
public interface IProducer {
    String createText(int length);

    BufferedImage createImage(int width, int height, String text);

    ByteOutputStream createOutStream(int width, int height, String formatName, String text);

    String createBase64(int width, int height, String formatName, String text);
}
