package liangchen.wang.gradf.framework.data.dao;


import liangchen.wang.gradf.framework.data.entity.Columns;

/**
 * @author LiangChen.Wang
 */
public interface IDesignerDao {
    Columns columns(String tableName);
}
