package liangchen.wang.gradf.component.foura.manager.domain.result;

import liangchen.wang.gradf.component.business.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2020-07-29 13:56:01
*/
public class UrlRelationResultDomain extends ResultDomain {
    private static final UrlRelationResultDomain self = new UrlRelationResultDomain();

    public static UrlRelationResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.classCast(self.clone());
    }

    private Long role_id;
    private Long resource_id;
    private Long operation_id;
    private Long url_id;

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
    }

    public Long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Long operation_id) {
        this.operation_id = operation_id;
    }

    public Long getUrl_id() {
        return url_id;
    }

    public void setUrl_id(Long url_id) {
        this.url_id = url_id;
    }

}
