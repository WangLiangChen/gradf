package liangchen.wang.gradf.framework.data.pagination;

import java.util.Map;

/**
 * @author LiangChen.Wang
 * @date 18-11-14 上午11:39
 */
public class QueryParameter {
    private Map<String,String> search;
    private PaginationParameter pagination;

    public Map<String, String> getSearch() {
        return search;
    }

    public void setSearch(Map<String, String> search) {
        this.search = search;
    }

    public PaginationParameter getPagination() {
        return pagination;
    }

    public void setPagination(PaginationParameter pagination) {
        this.pagination = pagination;
    }
}
