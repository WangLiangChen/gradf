package liangchen.wang.gradf.framework.data.pagination;

import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author LiangChen.Wang
 */
public class PaginationResult<E> {
	public static <E> PaginationResult<E> newInstance() {
		return new PaginationResult<>();
	}

	private List<E> datas;
	private Integer totalRecords;
	private Integer page;
	private Integer rows;

	public List<E> getDatas() {
		return datas;
	}
	public void setDatas(List<E> datas) {
		this.datas = datas;
	}
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Integer totalRecord) {
		this.totalRecords = totalRecord;
	}
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public void circleDatas(Consumer<E> consumer){
	    if(CollectionUtil.INSTANCE.isEmpty(datas)){
	        return;
        }
	    datas.forEach(d->consumer.accept(d));
    }
	public <T> PaginationResult<T> copyTo(Class<T> clazz){
		return copyTo(clazz,null);
	}
	public <T> PaginationResult<T> copyTo(Class<T> clazz,Consumer<T> consumer){
	    PaginationResult<T> paginationResult = new PaginationResult<>();
	    paginationResult.setTotalRecords(this.totalRecords);
        paginationResult.setPage(this.page);
        paginationResult.setRows(this.rows);
	    if(CollectionUtil.INSTANCE.isEmpty(datas)){
	    	paginationResult.setDatas(Collections.emptyList());
	    	return paginationResult;
		}
        List<T> ts = new ArrayList<>(datas.size());
		datas.forEach(d->{
             T t = ClassBeanUtil.INSTANCE.copyProperties(d,clazz);
             if(null!=consumer){
             	consumer.accept(t);
			 }
             ts.add(t);
		});
		paginationResult.setDatas(ts);
	    return paginationResult;
    }
    @Override
    public String toString() {
       return JsonUtil.INSTANCE.toJSONString(this);
    }
}
