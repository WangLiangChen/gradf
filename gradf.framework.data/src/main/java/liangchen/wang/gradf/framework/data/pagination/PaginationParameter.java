package liangchen.wang.gradf.framework.data.pagination;

import liangchen.wang.crdf.framework.commons.object.EnhancedObject;
import liangchen.wang.crdf.framework.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiangChen.Wang
 */
public class PaginationParameter extends EnhancedObject {
    /**
     * 分页页号
     */
	private transient Integer page;
    /**
     * 分页记录偏移(MySql)
     */
	private transient Integer offset;
    /**
     * 行数
     */
	private transient Integer rows;
    /**
     * 排序
     */
	private transient List<OrderBy> orderBy;

	public void initPagination() {
		this.page = null == this.page ? 1 : this.page;
		this.rows = null == this.rows ? 10 : this.rows;
	}

	public List<OrderBy> getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(List<OrderBy> orderBy){this.orderBy = orderBy;}
	public void addOrderBy(String orderby, OrderByDirection direction) {
		addOrderBy(orderby, direction, null);
	}
	public void addOrderBy(String orderby, OrderByDirection direction, Integer index) {
	    if(StringUtil.INSTANCE.isBlank(orderby)){
	        return;
        }
        if(null==direction){
	        direction = OrderByDirection.asc;
        }
		orderBy=orderBy==null?new ArrayList<>(1):orderBy;
		if (null == index) {
			orderBy.add(new OrderBy(orderby, direction));
			return;
		}
		orderBy.add(index, new OrderBy(orderby, direction));
	}

	public Integer getPage() {
		if (null != page) {
			page = page < 1 ? 1 : page;
			return page;
		}
		if (null != offset && null != rows) {
			page = offset / rows + 1;
		}
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getOffset() {
		if (null != offset) {
			offset = offset < 0 ? 0 : offset;
			return offset;
		}
		if (null != page && null != rows) {
			offset = (page - 1) * rows;
		}
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getRows() {
		if (null != rows && rows < 1) {
			return 1;
		}
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
}
