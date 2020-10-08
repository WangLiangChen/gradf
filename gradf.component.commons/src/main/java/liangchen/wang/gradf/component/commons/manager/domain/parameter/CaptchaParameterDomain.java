package liangchen.wang.gradf.component.commons.manager.domain.parameter;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2019/7/5 10:43
 */
public class CaptchaParameterDomain {
    @NotNull(message = "图片宽度不能为空")
    @Range(min = 50, max = 200, message = "图片宽度50-200")
    private Integer width;
    @NotNull(message = "图片高度不能为空")
    @Range(min = 20, max = 100, message = "图片高度20-100")
    private Integer height;
    @NotEmpty(message = "业务类型不能为空")
    private String business_type;
    private String business_data;
    @Range(min = 4, max = 8, message = "字符长度4-8")
    private Byte length = 4;

    public static CaptchaParameterDomain newInstance() {
        return new CaptchaParameterDomain();
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public Byte getLength() {
        return length;
    }

    public void setLength(Byte length) {
        this.length = length;
    }
}
