package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.component.foura.dao.entity.Url;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.validator.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2020-04-13 00:40:41
 */
public class UrlParameterDomain extends ParameterDomain<Url> {
    private static final UrlParameterDomain self = new UrlParameterDomain();
    @NotNull(message = "url_id不能为空", groups = {UpdateGroup.class})
    private Long url_id;
    @NotBlank(message = "Url说明不能为空")
    private String url_text;
    @NotBlank(message = "Url路径不能为空")
    private String url_path;
    private String summary;

    public static UrlParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getUrl_id() {
        return url_id;
    }

    public void setUrl_id(Long url_id) {
        this.url_id = url_id;
    }


    public String getUrl_text() {
        return url_text;
    }

    public void setUrl_text(String url_text) {
        this.url_text = url_text;
    }

    public String getUrl_path() {
        return url_path;
    }

    public void setUrl_path(String url_path) {
        this.url_path = url_path;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
