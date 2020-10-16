package liangchen.wang.gradf.component.foura.manager.impl;


import liangchen.wang.gradf.component.commons.captcha.CaptchaValidateException;
import liangchen.wang.gradf.component.commons.manager.ICaptchaManager;
import liangchen.wang.gradf.component.foura.annotation.EnableOperationLog;
import liangchen.wang.gradf.component.foura.annotation.OperationLog;
import liangchen.wang.gradf.component.foura.dao.IAccountDao;
import liangchen.wang.gradf.component.foura.event.AccountLoginEvent;
import liangchen.wang.gradf.component.foura.manager.IAccountManager;
import liangchen.wang.gradf.component.foura.manager.IAuthenticationManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.UsernamePasswordLoginParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountLoginResultDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountPasswordResultDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountResultDomain;
import liangchen.wang.gradf.component.foura.utils.OperationLogUtil;
import liangchen.wang.gradf.component.foura.utils.PasswordUtil;
import liangchen.wang.gradf.framework.commons.exception.ExceptionData;
import liangchen.wang.gradf.framework.commons.exception.PromptException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.springboot.event.EventPublisher;
import liangchen.wang.gradf.framework.web.jwts.AccessToken;
import liangchen.wang.gradf.framework.web.jwts.AccessTokenUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author LiangChen.Wang
 */
@Component("Gradf_Foura_DefaultAuthenticationManager")
@EnableOperationLog(businessType = "Authentication", businessName = "认证")
public class AuthenticationManagerImpl implements IAuthenticationManager {
    private final IAccountManager accountManager;
    private final ICaptchaManager captchaManager;
    private final IAccountDao accountDao;

    @Inject
    public AuthenticationManagerImpl(@Named("Gradf_Foura_DefaultAccountManager") IAccountManager accountManager,
                                     @Named("Gradf_Commons_CaptchaManager") ICaptchaManager captchaManager,
                                     @Named("Gradf_Foura_DefaultAccountDao") IAccountDao accountDao) {
        this.accountManager = accountManager;
        this.captchaManager = captchaManager;
        this.accountDao = accountDao;
    }

    @Override
    public AccountPasswordResultDomain authenticationInfoByKey(String accountKey) {
        Long account_id = accountManager.idByKey(accountKey);
        if (null == account_id) {
            ExceptionData exceptionData = ExceptionData.newInstance("AuthenticationFailed");
            throw new PromptException(exceptionData, "用户或密码错误");
        }
        return authenticationInfoById(account_id);
    }

    @Override
    public AccountPasswordResultDomain authenticationInfoById(Long account_id) {
        AccountResultDomain accountResultDomain = accountManager.byPrimaryKeyOrThrow(account_id, new String[]{Status.NORMAL.name()}, null, "account_id", "login_name", "nick_name", "mobile", "email", "login_password", "password_salt", "jwt_key", "expire_datetime", "password_expire");
        return accountResultDomain.copyTo(AccountPasswordResultDomain.class);
    }

    @Override
    @OperationLog(operationFlag = "Login", operationName = "登录")
    public AccountLoginResultDomain loginWithUsernamePassword(UsernamePasswordLoginParameterDomain parameterDomain) {
        Assert.INSTANCE.validate(parameterDomain);
        // 验证验证码
        try {
            captchaManager.validate(parameterDomain.getCaptchaKey(), parameterDomain.getCaptcha(), "LOGIN");
        } catch (CaptchaValidateException e) {
            ExceptionData exceptionData = ExceptionData.newInstance("AuthenticationFailed");
            throw new PromptException(exceptionData, e.getMessage());
        }
        String accountkey = parameterDomain.getUsername();
        // 验证用户
        AccountPasswordResultDomain accountPassword = authenticationInfoByKey(accountkey);
        if (null == accountPassword) {
            ExceptionData exceptionData = ExceptionData.newInstance("AuthenticationFailed");
            throw new PromptException(exceptionData, "用户或密码错误");
        }
        // 验证密码
        String inputPassword = parameterDomain.getPassword();
        inputPassword = PasswordUtil.INSTANCE.encryptPassword(accountPassword.getAccount_id(), inputPassword, accountPassword.getPassword_salt());
        if (!inputPassword.equals(accountPassword.getLogin_password())) {
            ExceptionData exceptionData = ExceptionData.newInstance("AuthenticationFailed");
            throw new PromptException(exceptionData, "用户或密码错误");
        }
        // 生成token
        Long account_id = accountPassword.getAccount_id();
        String jwtKey = accountPassword.getJwt_key();
        if (StringUtil.INSTANCE.isBlank(jwtKey)) {
            jwtKey = AccessTokenUtil.DEFAULT_KEY;
        }
        AccessToken accessToken = AccessToken.newInstance();
        accessToken.put("account_id", account_id);
        String access_token = AccessTokenUtil.INSTANCE.create(accessToken, jwtKey);
        // 为稳妥起见置空关键属性
        accountPassword.setJwt_key(null);
        accountPassword.setLogin_password(null);
        accountPassword.setPassword_salt(null);
        AccountLoginResultDomain accountLoginResultDomain = accountPassword.copyTo(AccountLoginResultDomain.class);
        accountLoginResultDomain.setAccess_token(access_token);
        // 派发登录事件
        EventPublisher.INSTANCE.publishEvent(new AccountLoginEvent(this, accountLoginResultDomain));
        // 设置日志数据
        OperationLogUtil.INSTANCE.setBusinessId(account_id);
        return accountLoginResultDomain;
    }
}
