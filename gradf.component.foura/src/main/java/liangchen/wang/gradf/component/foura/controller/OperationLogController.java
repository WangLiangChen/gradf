package liangchen.wang.gradf.component.foura.controller;

import liangchen.wang.gradf.component.foura.dao.query.OperationLogQuery;
import liangchen.wang.gradf.component.foura.manager.IOperationLogManager;
import liangchen.wang.gradf.component.foura.manager.domain.result.OperationLogResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;

@RestController("Gradf_OperationLog_Controller")
@RequestMapping(value = "/auth/gradf/log")
public class OperationLogController {
    private final IOperationLogManager manager;

    @Inject
    public OperationLogController(@Named("Gradf_Business_DefaultOperationLogManager") IOperationLogManager manager) {
        this.manager = manager;
    }

    @PostMapping("/pagination")
    public void pagination(@RequestBody OperationLogQuery query) {
        PaginationResult<OperationLogResultDomain> pagination = manager.pagination(query);
        ResponseUtil.createResponse().data(pagination).flush();
    }
}
