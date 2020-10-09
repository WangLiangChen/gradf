package liangchen.wang.gradf.component.business.controller;

import liangchen.wang.gradf.component.business.dao.query.RegionQuery;
import liangchen.wang.gradf.component.business.manager.IRegionManager;
import liangchen.wang.gradf.component.business.manager.domain.result.RegionResultDomain;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang
 */
@RestController
@RequestMapping(value = "/gradf/region")
public class RegionController {
    private final IRegionManager manager;

    @Inject
    public RegionController(@Named("Gradf_Business_DefaultRegionManager") IRegionManager manager) {
        this.manager = manager;
    }

    @GetMapping("/provinces")
    public void provinces() {
        byParentCode(0L);
    }

    @GetMapping("/cities")
    public void cities(Long province_code) {
        byParentCode(province_code);
    }

    @GetMapping("/counties")
    public void counties(Long city_code) {
        byParentCode(city_code);
    }

    @GetMapping("/byParentCode")
    public void byParentCode(Long parent_code) {
        parent_code = parent_code == null ? 0 : parent_code;
        RegionQuery query = RegionQuery.newInstance();
        query.setParentCode(parent_code);
        query.setStatus(Status.NORMAL.name());
        List<RegionResultDomain> regions = manager.list(query);
        //去除中国
        regions = regions.parallelStream().filter(e -> e.getRegion_code() > 0).collect(Collectors.toList());
        ResponseUtil.createResponse().data(regions).flush();
    }

    @PostMapping("/pagination")
    public void pagination(@RequestBody RegionQuery query) {
        PaginationResult<RegionResultDomain> pagination = manager.pagination(query);
        ResponseUtil.createResponse().data(pagination).flush();
    }
}
