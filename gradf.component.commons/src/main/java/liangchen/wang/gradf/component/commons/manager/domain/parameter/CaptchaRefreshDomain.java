package liangchen.wang.gradf.component.commons.manager.domain.parameter;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2019/7/5 10:43
 */
public class CaptchaRefreshDomain {
    @NotNull(message = "图片宽度不能为空")
    @Range(min = 50, max = 200, message = "图片宽度50-200")
    private Integer width;
    @NotNull(message = "图片高度不能为空")
    @Range(min = 20, max = 100, message = "图片高度20-100")
    private Integer height;
    @NotEmpty(message = "刷新标识不能为空")
    private String refresh_key;
    @Range(min = 4, max = 8, message = "字符长度4-8")
    private Byte length = 4;

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

    public String getRefresh_key() {
        return refresh_key;
    }

    public void setRefresh_key(String refresh_key) {
        this.refresh_key = refresh_key;
    }

    public Byte getLength() {
        return length;
    }

    public void setLength(Byte length) {
        this.length = length;
    }
}
