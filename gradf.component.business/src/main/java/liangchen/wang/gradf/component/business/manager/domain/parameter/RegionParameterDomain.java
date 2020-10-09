package liangchen.wang.gradf.component.business.manager.domain.parameter;

import liangchen.wang.gradf.component.business.dao.entity.Region;
import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2019-12-29 20:30:26
 */
public class RegionParameterDomain extends ParameterDomain<Region> {
    private static final RegionParameterDomain self = new RegionParameterDomain();

    public static RegionParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long region_code;
    private Long parent_code;
    @NotNull(message = "区域名称不能为空")
    private String region_name;
    private Long sort;
    private String short_name;
    private String area_code;
    private String pinyin;
    private String jianpin;
    private String status;

    public Long getRegion_code() {
        return region_code;
    }

    public void setRegion_code(Long region_code) {
        this.region_code = region_code;
    }

    public Long getParent_code() {
        return parent_code;
    }

    public void setParent_code(Long parent_code) {
        this.parent_code = parent_code;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }


    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getJianpin() {
        return jianpin;
    }

    public void setJianpin(String jianpin) {
        this.jianpin = jianpin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
