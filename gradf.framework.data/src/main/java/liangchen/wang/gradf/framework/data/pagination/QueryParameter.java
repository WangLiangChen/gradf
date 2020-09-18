package liangchen.wang.gradf.framework.data.pagination;

import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.util.Map;

/**
 * @author LiangChen.Wang
 * @date 18-11-14 上午11:39
 */
public class QueryParameter {
    private Map<String, String> search;
    private PaginationParameter pagination;

    public Map<String, String> getSearch() {
        return search;
    }

    public void setSearch(Map<String, String> search) {
        Assert.INSTANCE.notEmpty(search, "参数search不能为空");
        this.search = search;
    }

    public PaginationParameter getPagination() {
        return pagination;
    }

    public void setPagination(PaginationParameter pagination) {
        Assert.INSTANCE.notNull(pagination, "参数pagination不能为空");
        this.pagination = pagination;
    }
}
