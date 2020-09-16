package liangchen.wang.gradf.framework.data.dao;


import liangchen.wang.crdf.framework.data.entity.Columns;

/**
 * @author LiangChen.Wang
 */
public interface IDesignerDao {
	Columns columns(String tableName);
}
