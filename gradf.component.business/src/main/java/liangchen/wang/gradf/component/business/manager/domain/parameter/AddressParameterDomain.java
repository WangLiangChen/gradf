package liangchen.wang.gradf.component.business.manager.domain.parameter;

import liangchen.wang.gradf.component.business.dao.entity.Address;
import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2020-03-24 08:35:30
*/
public class AddressParameterDomain extends ParameterDomain<Address> {
    private static final AddressParameterDomain self = new AddressParameterDomain();

    public static AddressParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long address_id;
    private Byte province_id;
    private Short city_id;
    private Integer county_id;
    private Integer town_id;
    private Long village_id;
    private String address_zipcode;
    private java.math.BigDecimal address_lon;
    private java.math.BigDecimal address_lat;
    private java.math.BigDecimal mercator_x;
    private java.math.BigDecimal mercator_y;
    private String address_geohash;
    private String address_text;
    private String contact_person;
    private String contact_mobile;
    private String contact_tel;
    private String contact_email;
    private String address_summary;
    private Long sort;
    private String summary;
    private String status;
    private Long creator;
    private Long modifier;

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }

    public Byte getProvince_id() {
        return province_id;
    }

    public void setProvince_id(Byte province_id) {
        this.province_id = province_id;
    }

    public Short getCity_id() {
        return city_id;
    }

    public void setCity_id(Short city_id) {
        this.city_id = city_id;
    }

    public Integer getCounty_id() {
        return county_id;
    }

    public void setCounty_id(Integer county_id) {
        this.county_id = county_id;
    }

    public Integer getTown_id() {
        return town_id;
    }

    public void setTown_id(Integer town_id) {
        this.town_id = town_id;
    }

    public Long getVillage_id() {
        return village_id;
    }

    public void setVillage_id(Long village_id) {
        this.village_id = village_id;
    }

    public String getAddress_zipcode() {
        return address_zipcode;
    }

    public void setAddress_zipcode(String address_zipcode) {
        this.address_zipcode = address_zipcode;
    }

    public java.math.BigDecimal getAddress_lon() {
        return address_lon;
    }

    public void setAddress_lon(java.math.BigDecimal address_lon) {
        this.address_lon = address_lon;
    }

    public java.math.BigDecimal getAddress_lat() {
        return address_lat;
    }

    public void setAddress_lat(java.math.BigDecimal address_lat) {
        this.address_lat = address_lat;
    }

    public java.math.BigDecimal getMercator_x() {
        return mercator_x;
    }

    public void setMercator_x(java.math.BigDecimal mercator_x) {
        this.mercator_x = mercator_x;
    }

    public java.math.BigDecimal getMercator_y() {
        return mercator_y;
    }

    public void setMercator_y(java.math.BigDecimal mercator_y) {
        this.mercator_y = mercator_y;
    }

    public String getAddress_geohash() {
        return address_geohash;
    }

    public void setAddress_geohash(String address_geohash) {
        this.address_geohash = address_geohash;
    }

    public String getAddress_text() {
        return address_text;
    }

    public void setAddress_text(String address_text) {
        this.address_text = address_text;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getContact_tel() {
        return contact_tel;
    }

    public void setContact_tel(String contact_tel) {
        this.contact_tel = contact_tel;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getAddress_summary() {
        return address_summary;
    }

    public void setAddress_summary(String address_summary) {
        this.address_summary = address_summary;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }
}
