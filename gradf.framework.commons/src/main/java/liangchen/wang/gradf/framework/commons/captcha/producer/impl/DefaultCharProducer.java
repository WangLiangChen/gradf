package liangchen.wang.gradf.framework.commons.captcha.producer.impl;

import liangchen.wang.gradf.framework.commons.captcha.Configurable;
import liangchen.wang.gradf.framework.commons.captcha.producer.CharProducer;

import java.util.Random;

/**
 * @author LiangChen.Wang 2019/7/3 17:00
 */
public class DefaultCharProducer extends Configurable implements CharProducer {
    @Override
    public String getText(int length) {
        char[] chars = this.getProperties().getCharString();
        Random rand = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            text.append(chars[rand.nextInt(chars.length)]);
        }

        return text.toString();
    }
}
