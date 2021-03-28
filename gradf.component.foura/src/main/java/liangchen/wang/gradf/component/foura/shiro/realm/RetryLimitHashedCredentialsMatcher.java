package liangchen.wang.gradf.component.foura.shiro.realm;


import liangchen.wang.gradf.framework.cache.override.CacheManager;
import liangchen.wang.gradf.framework.commons.exception.PromptException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author LiangChen.Wang
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    private static final Logger logger = LoggerFactory.getLogger(RetryLimitHashedCredentialsMatcher.class);
    private final CacheManager cacheManager;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    // 这里暂时不考虑并发情况
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Object principal = token.getPrincipal();
        logger.debug("principal is : {}", principal);
        String loginKey = (String) principal;
        String retryCountKey = String.format("%s:RetryCount", loginKey);
        Cache cache = cacheManager.getCache(retryCountKey, 50*60000);
        int count = cache.get("count", int.class);
        cache.put("count", ++count);
        if (count > 5) {
            throw new PromptException("密码错误次数超过5次,请10分钟后再尝试登录");
        }
        boolean matches = super.doCredentialsMatch(token, info);
        logger.debug("Credentials Match:{}", matches);
        if (matches) {
            cache.clear();
        }
        return matches;
    }
}
