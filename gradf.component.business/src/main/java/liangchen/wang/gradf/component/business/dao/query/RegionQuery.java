package liangchen.wang.gradf.component.business.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.core.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2019-12-29 20:30:26
 */
@Table(name = "gradf_region")
public class RegionQuery extends RootQuery {
    private static final RegionQuery self = new RegionQuery();

    public static RegionQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Query(operator = Operator.EQUALS, column = "region_code")
    private Long region_code;
    @Query(operator = Operator.STARTWITH, column = "region_code")
    private Long region_name;
    @Query(operator = Operator.EQUALS, column = "parent_code")
    private Long parent_code;

    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;
    @Query(operator = Operator.EQUALS, column = "parent_code")
    private Long parentCode;
    @Query(operator = Operator.IN, column = "region_code")
    private Long[] regionCodes;

    public Long getRegion_code() {
        return region_code;
    }

    public void setRegion_code(Long region_code) {
        this.region_code = region_code;
    }

    public Long getRegion_name() {
        return region_name;
    }

    public void setRegion_name(Long region_name) {
        this.region_name = region_name;
    }

    public Long getParent_code() {
        return parent_code;
    }

    public void setParent_code(Long parent_code) {
        this.parent_code = parent_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getStatusIn() {
        return statusIn;
    }

    public void setStatusIn(String[] statusIn) {
        this.statusIn = statusIn;
    }

    public String[] getStatusNotIn() {
        return statusNotIn;
    }

    public void setStatusNotIn(String[] statusNotIn) {
        this.statusNotIn = statusNotIn;
    }

    public void setParentCode(Long parentCode) {
        this.parentCode = parentCode;
    }

    public Long getParentCode() {
        return parentCode;
    }

    public Long[] getRegionCodes() {
        return regionCodes;
    }

    public void setRegionCodes(Long[] regionCodes) {
        this.regionCodes = regionCodes;
    }
}
