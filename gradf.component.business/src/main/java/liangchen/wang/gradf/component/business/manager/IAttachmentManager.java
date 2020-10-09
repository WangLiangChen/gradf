package liangchen.wang.gradf.component.business.manager;

import liangchen.wang.gradf.component.business.dao.query.AttachmentQuery;
import liangchen.wang.gradf.component.business.manager.domain.parameter.AttachmentParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.AttachmentResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-02-11 09:14:24
*/
public interface IAttachmentManager  {

    boolean insert(AttachmentParameterDomain parameter);

    boolean deleteByPrimaryKey(Long attachment_id);

    boolean updateByPrimaryKey(AttachmentParameterDomain parameter);

    int updateByQuery(AttachmentParameterDomain parameter, AttachmentQuery query);

    boolean updateStatusByPrimaryKey(Long attachment_id, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long attachment_id, String statusTo, String[] statusIn, String[] statusNotIn);

    AttachmentResultDomain byPrimaryKey(Long attachment_id, String... returnFields);

    AttachmentResultDomain byPrimaryKeyOrThrow(Long attachment_id, String... returnFields);

    AttachmentResultDomain byPrimaryKeyOrThrow(Long attachment_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    AttachmentResultDomain byPrimaryKey(Long attachment_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<AttachmentResultDomain> list(AttachmentQuery query, String... returnFields);

    PaginationResult<AttachmentResultDomain> pagination(AttachmentQuery query, String... returnFields);

}
