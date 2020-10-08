package liangchen.wang.gradf.component.commons.controller;


import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.QRCodeUtil;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * @author LiangChen.Wang
 */
@RestController
@RequestMapping(value = "/gradf/qrcode")
public class QRCodeController {

    @GetMapping("/base64")
    public void base64(String text, Short size) {
        String base64 = QRCodeUtil.INSTANCE.createBase64(text, size);
        ResponseUtil.createResponse().data(base64).flush();
    }

    @GetMapping("/image")
    public void image(String text, Short size, HttpServletResponse response) {
        response.reset();
        response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");
        ServletOutputStream out = null;
        try {
            BufferedImage bufferedImage = QRCodeUtil.INSTANCE.createImage(text, size);
            out = response.getOutputStream();
            ImageIO.write(bufferedImage, "png", out);
            out.flush();
        } catch (Exception e) {
            throw new ErrorException(e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (Exception ex) {
                    throw new ErrorException(ex);
                }
            }
        }
    }
}
