package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.AccountQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.AccountParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.ModifyPasswordParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-19 10:03:02
 */
public interface IAccountManager {

    boolean insert(AccountParameterDomain parameter);

    boolean insert(AccountParameterDomain parameter, boolean requireChangePassword);

    boolean deleteByPrimaryKey(Long account_id);

    boolean updateNickName(Long account_id, String nick_name);

    boolean updateStatusByPrimaryKey(Long account_id, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long account_id, String statusTo, String[] statusIn, String[] statusNotIn);

    AccountResultDomain byPrimaryKey(Long account_id, String... returnFields);

    AccountResultDomain byPrimaryKeyOrThrow(Long account_id, String... returnFields);

    AccountResultDomain byPrimaryKeyOrThrow(Long account_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    AccountResultDomain byPrimaryKey(Long account_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<AccountResultDomain> list(AccountQuery query, String... returnFields);

    PaginationResult<AccountResultDomain> pagination(AccountQuery query, String... returnFields);

    Long idByKey(String accountKey);

    void validateExist(String accountKey);

    boolean exist(String accountKey);

    boolean exist(Long accountId);

    void resetPassword(Long account_id, String password);

    void resetPassword(Long account_id, String password, boolean forcePasswordChange);

    void modifyPassword(ModifyPasswordParameterDomain parameter);
}
