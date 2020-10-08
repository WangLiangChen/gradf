package liangchen.wang.gradf.component.commons.manager.impl;


import liangchen.wang.gradf.component.commons.captcha.CaptchaValidateException;
import liangchen.wang.gradf.component.commons.captcha.ValidateData;
import liangchen.wang.gradf.component.commons.manager.ICaptchaManager;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.CaptchaParameterDomain;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.CaptchaRefreshDomain;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.CaptchaValidateDomain;
import liangchen.wang.gradf.component.commons.manager.domain.result.CaptchaResultDomain;
import liangchen.wang.gradf.framework.cache.primary.GradfCacheManager;
import liangchen.wang.gradf.framework.commons.captcha.CaptchaProperties;
import liangchen.wang.gradf.framework.commons.captcha.producer.impl.DefaultProducer;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.exception.ExceptionData;
import liangchen.wang.gradf.framework.commons.exception.PromptException;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2019/6/28 14:51
 */
@Component("Gradf_Commons_CaptchaManager")
public class CaptchaManagerImpl implements ICaptchaManager {
    private final static CaptchaProperties captchaProperties = CaptchaProperties.newInstance();
    private static final String DEFAULTCHARS = "abcdefghjkmnpqrstuvwxyz23456789ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final DefaultProducer producer;

    private final GradfCacheManager cacheManager;
    private final Cache cache;

    @Inject
    public CaptchaManagerImpl(GradfCacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.cache = cacheManager.getCache("Gradf_Captcha", 5, TimeUnit.MINUTES);
    }

    static {
        captchaProperties.put("char.string", DEFAULTCHARS);
        captchaProperties.put("char.space", "10");
        captchaProperties.put("char.color", "black");
        captchaProperties.put("char.fonts", "Arial,Microsoft YaHei");
        captchaProperties.put("char.producer", "liangchen.wang.gradf.component.business.captcha.producer.impl.DefaultCharProducer");
        captchaProperties.put("char.renderer", "liangchen.wang.gradf.component.business.captcha.renderer.impl.DefaultCharRenderer");
        captchaProperties.put("effector.renderer", "liangchen.wang.gradf.component.business.captcha.renderer.impl.FishEyeEffectorRenderer");
        //captchaProperties.put("noise.color", "black");
        //captchaProperties.put("noise.renderer", "liangchen.wang.gradf.component.business.captcha.renderer.impl.DefaultNoiseRenderer");
        producer = new DefaultProducer();
        producer.setProperties(captchaProperties);
    }

    @Override
    public CaptchaResultDomain createCaptcha(CaptchaParameterDomain parameterDomain) {
        Assert.INSTANCE.validate(parameterDomain);
        String chars = producer.createText(parameterDomain.getLength());
        BufferedImage bufferedImage = producer.createImage(parameterDomain.getWidth(), parameterDomain.getHeight(), chars);
        CaptchaResultDomain captchaResultDomain = CaptchaResultDomain.newInstance();
        //Base64编码
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            String captcha_base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            captcha_base64 = String.format("data:image/png;base64,%s", captcha_base64);
            captchaResultDomain.setCaptcha_base64(captcha_base64);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        String captcha_key = UUID.randomUUID().toString();
        captchaResultDomain.setCaptcha_key(captcha_key);
        saveValidateData(captcha_key, chars, parameterDomain.getBusiness_type(), parameterDomain.getBusiness_data());
        return captchaResultDomain;
    }

    @Override
    public CaptchaResultDomain refresh(CaptchaRefreshDomain refreshDomain) {
        Assert.INSTANCE.validate(refreshDomain);
        String refresh_key = refreshDomain.getRefresh_key();
        //根据key查询缓存数据
        ValidateData validateData = findValidateData(refresh_key);
        Assert.INSTANCE.notNull(validateData, "原有验证码不存在");
        //删除原有验证码
        deleteValidateData(refresh_key);
        //创建新的验证码
        CaptchaParameterDomain parameterDomain = CaptchaParameterDomain.newInstance();
        parameterDomain.setBusiness_type(validateData.getBusiness_type());
        parameterDomain.setBusiness_data(validateData.getBusiness_data());
        parameterDomain.setWidth(refreshDomain.getWidth());
        parameterDomain.setHeight(refreshDomain.getHeight());
        return createCaptcha(parameterDomain);
    }

    @Override
    public void validate(CaptchaValidateDomain validateDomain) {
        Assert.INSTANCE.validate(validateDomain);
        try {
            validate(validateDomain.getCaptcha_key(), validateDomain.getCaptcha(), validateDomain.getBusiness_type(), validateDomain.getBusiness_data(), false);
        } catch (CaptchaValidateException e) {
            throw new PromptException(ExceptionData.newInstance("ValidateError"), e.getMessage());
        }
    }

    @Override
    public void validate(String key, String code, String business_type) {
        validate(key, code, business_type, null, true);
    }

    @Override
    public void validate(String key, String code, String business_type, String business_data, boolean deleteOnSuccess) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        Assert.INSTANCE.notBlank(code, "code不能为空");
        Assert.INSTANCE.notBlank(business_type, "business_type不能为空");
        ValidateData validateData = findValidateData(key);
        if (null == validateData) {
            throw new CaptchaValidateException("验证码不存在");
        }
        String savedBusinessType = validateData.getBusiness_type();
        if (null != savedBusinessType && savedBusinessType.length() > 0 && !savedBusinessType.equals(business_type)) {
            throw new CaptchaValidateException("验证码业务类型不匹配");
        }
        String savedBusinessData = validateData.getBusiness_data();
        if (null != savedBusinessData && savedBusinessData.length() > 0 && !savedBusinessData.equals(business_data)) {
            throw new CaptchaValidateException("验证码业务数据不匹配");
        }
        String savedCode = validateData.getCode();
        if (!code.equalsIgnoreCase(savedCode)) {
            throw new CaptchaValidateException("验证码错误");
        }
        //校验通过删除校验数据
        if (deleteOnSuccess) {
            deleteValidateData(key);
        }
    }


    private void deleteValidateData(String key) {
        cache.evict(key);
    }

    private void saveValidateData(String key, String code) {
        ValidateData validateData = ValidateData.newInstance();
        validateData.setCode(code);
        saveValidateData(key, validateData);
    }

    private void saveValidateData(String key, String code, String business_type, String business_data) {
        ValidateData validateData = ValidateData.newInstance();
        validateData.setCode(code);
        validateData.setBusiness_type(business_type);
        validateData.setBusiness_data(business_data);
        saveValidateData(key, validateData);
    }

    private void saveValidateData(String key, ValidateData validateData) {
        String jsonString = JsonUtil.INSTANCE.toJsonString(validateData);
        cache.put(key, jsonString);
    }

    private ValidateData findValidateData(String key) {
        String jsonString = cache.get(key, String.class);
        return JsonUtil.INSTANCE.parseObject(jsonString, ValidateData.class);
    }
}
