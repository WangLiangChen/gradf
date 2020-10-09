package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IAccountDao;
import liangchen.wang.gradf.component.foura.dao.IAccountLoginDao;
import liangchen.wang.gradf.component.foura.dao.entity.Account;
import liangchen.wang.gradf.component.foura.dao.entity.AccountLogin;
import liangchen.wang.gradf.component.foura.dao.query.AccountLoginQuery;
import liangchen.wang.gradf.component.foura.dao.query.AccountQuery;
import liangchen.wang.gradf.component.foura.enumeration.LoginModeEnum;
import liangchen.wang.gradf.component.foura.manager.IAccountManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.AccountParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.ModifyPasswordParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountResultDomain;
import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import liangchen.wang.gradf.component.foura.utils.PasswordUtil;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.exception.PromptException;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-19 10:03:02
 */
@Component("Gradf_Foura_DefaultAccountManager")
public class AccountManagerImpl extends AbstractManager<Account, AccountQuery, AccountResultDomain> implements IAccountManager {
    private final IAccountLoginDao accountLoginDao;

    @Inject
    public AccountManagerImpl(@Named("Gradf_Foura_DefaultAccountDao") IAccountDao dao,
                              @Named("Gradf_Foura_DefaultAccountLoginDao") IAccountLoginDao accountLoginDao) {
        super("账户", "Account", dao);
        this.accountLoginDao = accountLoginDao;
    }

    @Override
    @Transactional
    public boolean insert(AccountParameterDomain parameter) {
        return insert(parameter, false);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean insert(AccountParameterDomain parameter, boolean requireChangePassword) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        String login_name = parameter.getLogin_name();
        String mobile = parameter.getMobile();
        String email = parameter.getEmail();
        if (StringUtil.INSTANCE.isBlank(login_name) && StringUtil.INSTANCE.isBlank(mobile) && StringUtil.INSTANCE.isBlank(email)) {
            throw new InfoException("登录名,手机号,邮箱不能同时为空");
        }
        Long account_id = parameter.getAccount_id();
        if (account_id == null) {
            account_id = UidDb.INSTANCE.uid();
        }
        // 插入登录名，登录方式并验证重复
        AccountLogin accountLogin = AccountLogin.newInstance();
        accountLogin.setAccount_id(account_id);
        try {
            // login_name
            if (StringUtil.INSTANCE.isNotBlank(login_name)) {
                accountLogin.setLogin_name(login_name);
                accountLogin.setLogin_mode(LoginModeEnum.LOGIN_NAME.name());
                accountLoginDao.insert(accountLogin);
            }
            // email
            if (StringUtil.INSTANCE.isNotBlank(email)) {
                accountLogin.setLogin_name(email);
                accountLogin.setLogin_mode(LoginModeEnum.EMAIL.name());
                accountLoginDao.insert(accountLogin);
            }
            // mobile
            if (StringUtil.INSTANCE.isNotBlank(mobile)) {
                accountLogin.setLogin_name(mobile);
                accountLogin.setLogin_mode(LoginModeEnum.MOBILE.name());
                accountLoginDao.insert(accountLogin);
            }
        } catch (DuplicateKeyException e) {
            throw new PromptException("账户已存在");
        }
        // 插入账户
        parameter.setAccount_id(account_id);
        parameter.populateEntity((account) -> {
            Assert.INSTANCE.notBlankElseRun(account.getStatus(), () -> account.setStatus(Status.NORMAL.name()));
            Assert.INSTANCE.notBlankElseRun(account.getNick_name(), () -> account.setNick_name("NickName"));
            LocalDateTime datetime = LocalDateTime.now();
            account.setExpire_datetime(datetime.plusYears(1));
            if (requireChangePassword) {
                account.setPassword_expire(datetime);
            } else {
                account.setPassword_expire(account.getExpire_datetime());
            }
            // password
            PasswordUtil.Password p = PasswordUtil.INSTANCE.encryptPassword(account.getAccount_id(), account.getLogin_password());
            account.setLogin_password(p.getPassword());
            account.setPassword_salt(p.getPassword_salt());
            // make secret_key by random
            account.setSecret_key(PasswordUtil.INSTANCE.randomSalt());
            account.initOperator();
            account.initFields();
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long account_id) {
        return updateStatusByPrimaryKey(account_id, Status.DELETED.name());
    }

    @Override
    public boolean updateNickName(Long account_id, String nick_name) {
        Assert.INSTANCE.notNull(account_id, "account_id不能为空");
        Assert.INSTANCE.notBlank(nick_name, "nick_name不能为空");
        AccountQuery query = AccountQuery.newInstance();
        query.setAccount_id(account_id);
        AccountParameterDomain parameter = AccountParameterDomain.newInstance();
        parameter.setNick_name(nick_name);
        parameter.populateEntity((account) -> {
            account.setModify_datetime(LocalDateTime.now());
            account.setModifier(ContextUtil.INSTANCE.getOperator());
        });
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long account_id, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(account_id, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long account_id, String statusTo, String[] statusIn, String[] statusNotIn) {
        Assert.INSTANCE.notNull(account_id, "account_id不能为空");
        Assert.INSTANCE.notBlank(statusTo, "statusTo不能为空");
        AccountParameterDomain parameter = AccountParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        AccountQuery query = AccountQuery.newInstance();
        query.setAccount_id(account_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        int rows = super.updateByQuery(parameter, query);
        return rows == 1 ? true : false;
    }

    @Override
    public AccountResultDomain byPrimaryKey(Long account_id, String... returnFields) {
        return byPrimaryKey(account_id, null, null, returnFields);
    }

    @Override
    public AccountResultDomain byPrimaryKeyOrThrow(Long account_id, String... returnFields) {
        return byPrimaryKeyOrThrow(account_id, null, null, returnFields);
    }

    @Override
    public AccountResultDomain byPrimaryKeyOrThrow(Long account_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        AccountResultDomain resultDomain = byPrimaryKey(account_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public AccountResultDomain byPrimaryKey(Long account_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notNull(account_id, "account_id不能为空");
        AccountQuery query = AccountQuery.newInstance();
        query.setAccount_id(account_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        return super.one(query, returnFields);
    }

    @Override
    public List<AccountResultDomain> list(AccountQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<AccountResultDomain> pagination(AccountQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

    @Override
    public Long idByKey(String accountKey) {
        Assert.INSTANCE.notBlank(accountKey, "账户Key不能为空");
        AccountLoginQuery query = AccountLoginQuery.newInstance();
        query.setLogin_name(accountKey);
        AccountLogin accountLogin = accountLoginDao.one(query, "account_id");
        Assert.INSTANCE.notNull(accountLogin, "账户不存在");
        return accountLogin.getAccount_id();
    }

    @Override
    public void validateExist(String accountKey) {
        boolean exist = exist(accountKey);
        Assert.INSTANCE.isFalse(exist, AssertLevel.PROMPT, "账户已存在");
    }

    @Override
    public boolean exist(String accountKey) {
        Assert.INSTANCE.notBlank(accountKey, "账户Key不能为空");
        AccountLoginQuery query = AccountLoginQuery.newInstance();
        query.setLogin_name(accountKey);
        return accountLoginDao.exist(query);
    }

    @Override
    public boolean exist(Long accountId) {
        Assert.INSTANCE.notNull(accountId, "账户ID不能为空");
        AccountQuery query = AccountQuery.newInstance();
        query.setAccount_id(accountId);
        return super.exist(query);
    }

    @Override
    public void resetPassword(Long account_id, String password) {
        resetPassword(account_id, password, false);
    }

    @Override
    public void resetPassword(Long account_id, String password, boolean requireChangePassword) {
        Assert.INSTANCE.notNull(account_id, "account_id不能为空");
        Assert.INSTANCE.notBlank(password, "password不能为空");
        boolean exist = exist(account_id);
        Assert.INSTANCE.isTrue(exist, AssertLevel.PROMPT, "账户不存在");
        AccountParameterDomain parameter = AccountParameterDomain.newInstance();
        parameter.populateEntity((account) -> {
            PasswordUtil.Password p = PasswordUtil.INSTANCE.encryptPassword(account_id, password);
            account.setLogin_password(p.getPassword());
            account.setPassword_salt(p.getPassword_salt());
            account.setSecret_key(PasswordUtil.INSTANCE.randomSalt());
            if (requireChangePassword) {
                account.setPassword_expire(LocalDateTime.now());
            }
        });
        AccountQuery query = AccountQuery.newInstance();
        query.setAccount_id(account_id);
        super.updateByQuery(parameter, query);
    }

    @Override
    public void modifyPassword(ModifyPasswordParameterDomain parameter) {
        Assert.INSTANCE.validate(parameter);
        //验证原始密码
        Long operator = FouraUtil.INSTANCE.getOperator();
        AccountQuery query = new AccountQuery();
        query.setAccount_id(operator);
        IAccountDao dao = this.getDao();
        Account account = dao.one(query, "login_password", "password_salt");
        String encryptPassword = PasswordUtil.INSTANCE.encryptPassword(operator, parameter.getOriginal_password(), account.getPassword_salt());
        Assert.INSTANCE.isEquals(encryptPassword, account.getLogin_password(), AssertLevel.PROMPT, "原密码错误");

        PasswordUtil.Password password = PasswordUtil.INSTANCE.encryptPassword(operator, parameter.getNew_password());
        account.setLogin_password(password.getPassword());
        account.setPassword_salt(password.getPassword_salt());
        account.setSecret_key(PasswordUtil.INSTANCE.randomSalt());
        dao.updateByQuery(account, query);
    }
}
