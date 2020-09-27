package liangchen.wang.gradf.component.foura.manager.domain.result;

import liangchen.wang.gradf.component.business.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2020-04-13 00:40:41
 */
public class UrlResultDomain extends ResultDomain {
    private static final UrlResultDomain self = new UrlResultDomain();

    public static UrlResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.classCast(self.clone());
    }

    private Long url_id;
    private String url_text;
    private String url_path;
    private java.time.LocalDateTime create_datetime;
    private String summary;

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


    public java.time.LocalDateTime getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(java.time.LocalDateTime create_datetime) {
        this.create_datetime = create_datetime;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


}
