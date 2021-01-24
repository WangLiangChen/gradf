package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.data.condition.JdbcConditionAnnotation;
import liangchen.wang.gradf.framework.data.dao.IDesignerDao;
import liangchen.wang.gradf.framework.data.entity.Column;
import liangchen.wang.gradf.framework.data.entity.Columns;
import liangchen.wang.gradf.framework.data.utils.DatabaseUtil;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiangChen.Wang 2019/11/19 21:11
 */
@JdbcConditionAnnotation
@Repository("Gradf_Data_DesignerDao")
public class DesignerDaoImpl implements IDesignerDao {
    private final JdbcTemplate jdbcTemplate;

    @Inject
    public DesignerDaoImpl(@Named("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Columns columns(String tableName) {
        // 查询获取主键列名
        List<String> primaryKeyColumnNames = new ArrayList<>();
        jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                primaryKeyColumnNames.add(columnName);
            }
            rs.close();
            return null;
        });

        // 查询获取列
        ResultSetMetaData rsmd = jdbcTemplate.query("select * from " + tableName + " where 1=0", ResultSet::getMetaData);
        String columnName, jdbcTypeName, javaTypeName;
        Column column;
        List<Column> primaryKeys = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        try {
            for (int i = 1, j = rsmd.getColumnCount(); i <= j; i++) {
                columnName = rsmd.getColumnName(i);
                jdbcTypeName = rsmd.getColumnTypeName(i);
                //javaTypeName = rsmd.getColumnClassName(i);
                javaTypeName = DatabaseUtil.INSTANCE.jdbcType2JavaType(jdbcTypeName);
                if (primaryKeyColumnNames.contains(columnName)) {
                    column = Column.newInstance(columnName, jdbcTypeName, javaTypeName, true);
                    primaryKeys.add(column);
                } else {
                    column = Column.newInstance(columnName, jdbcTypeName, javaTypeName);
                    columns.add(column);
                }
            }
        } catch (SQLException ex) {
            throw new ErrorException(ex);
        }
        return Columns.newInstance(primaryKeys, columns);
    }
}
