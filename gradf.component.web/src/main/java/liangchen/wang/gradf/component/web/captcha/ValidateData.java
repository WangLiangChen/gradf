package liangchen.wang.gradf.component.web.captcha;

/**
 * @author LiangChen.Wang 2019/7/5 8:51
 */
public class ValidateData {
    private String code;
    /**
     * 对应的业务类型标识，如注册/登录/短信等
     */
    private String business_type;
    /**
     * 对应的业务数据，如短信时的手机号码
     */
    private String business_data;

    public static ValidateData newInstance() {
        return new ValidateData();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getBusiness_data() {
        return business_data;
    }

    public void setBusiness_data(String business_data) {
        this.business_data = business_data;
    }
}
