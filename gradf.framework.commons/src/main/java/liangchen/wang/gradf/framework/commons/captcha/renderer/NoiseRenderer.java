package liangchen.wang.gradf.framework.commons.captcha.renderer;

import java.awt.image.BufferedImage;

/**
 * @author LiangChen.Wang 2019/7/4 16:37
 * 噪点渲染器
 */
public interface NoiseRenderer {
    void render(BufferedImage image);
}
