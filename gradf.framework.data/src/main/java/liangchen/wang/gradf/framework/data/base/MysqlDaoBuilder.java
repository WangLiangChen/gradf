package liangchen.wang.gradf.framework.data.base;

import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.condition.DataConditionAnnotation;
import liangchen.wang.gradf.framework.data.entity.Between;
import liangchen.wang.gradf.framework.data.enumeration.AndOr;
import liangchen.wang.gradf.framework.data.enumeration.Operator;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.hibernate.metamodel.model.domain.internal.EntityTypeImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang
 */
@DataConditionAnnotation
@Repository("Gradf_Data_MysqlDaoBuilder")
public class MysqlDaoBuilder {
    protected final Logger logger = LoggerFactory.getLogger(MysqlDaoBuilder.class);
    private final static Map<String, Storer> storerMap = new ConcurrentHashMap<>(100);
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    protected String insertId(Class<? extends RootEntity> entityClass) {
        String entityClassName = entityClass.getName();
        String cacheKey = String.format("%s.%s", entityClassName, "insert");
        Storer storer = storerMap.get(cacheKey);
        if (null == storer) {
            storer = new Storer();
            Storer previous = storerMap.putIfAbsent(cacheKey, storer);
            if (null == previous) {
                logger.debug("......create insertId");
                EntityStorer entityStorer = buildEntityStorer(entityClass);
                Set<String> pks = entityStorer.getPks();
                Set<String> fields = entityStorer.getFields();
                StringBuilder sql = new StringBuilder();
                sql.append("<script>insert into ").append(entityStorer.getTableName()).append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
                pks.forEach(pk -> sql.append(pk).append(","));
                fields.forEach(field -> sql.append(field).append(","));
                sql.append("</trim>values<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
                pks.forEach(pk -> sql.append("#{").append(pk).append("},"));
                fields.forEach(field -> sql.append("#{").append(field).append("},"));
                sql.append("</trim></script>");
                buildMappedStatement(cacheKey, SqlCommandType.INSERT, sql.toString(), entityClass, Integer.class);
                storer.setMappedStatementId(cacheKey);
            } else {
                storer = previous;
            }
        }
        return storer.getMappedStatementId();
    }

    public <E extends RootEntity> String insertBatchId(Class<E> entityClass) {
        String entityClassName = entityClass.getName();
        String cacheKey = String.format("%s.%s", entityClassName, "insertBatch");
        Storer storer = storerMap.get(cacheKey);
        if (null == storer) {
            storer = new Storer();
            Storer previous = storerMap.putIfAbsent(cacheKey, storer);
            if (null == previous) {
                logger.debug("......create insertBatchId");
                EntityStorer entityStorer = buildEntityStorer(entityClass);
                Set<String> pks = entityStorer.getPks();
                Set<String> fields = entityStorer.getFields();
                StringBuilder sql = new StringBuilder();
                sql.append("<script>insert into ").append(entityStorer.getTableName()).append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
                pks.forEach(pk -> sql.append(pk).append(","));
                fields.forEach(field -> sql.append(field).append(","));
                sql.append("</trim>values");
                sql.append("<foreach collection=\"list\" item=\"item\" separator=\",\">");
                sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
                pks.forEach(pk -> sql.append("#{item.").append(pk).append("},"));
                fields.forEach(field -> sql.append("#{item.").append(field).append("},"));
                sql.append("</trim>");
                sql.append("</foreach>");
                sql.append("</script>");
                buildMappedStatement(cacheKey, SqlCommandType.INSERT, sql.toString(), entityClass, Integer.class);
                storer.setMappedStatementId(cacheKey);
            } else {
                storer = previous;
            }
        }
        return storer.getMappedStatementId();
    }

    protected String deleteByQueryId(Class<? extends RootQuery> queryClass) {
        String tableName = tableNameByQueryClass(queryClass);
        String queryClassName = queryClass.getName();
        String cacheKey = String.format("%s.%s", queryClassName, "delete");
        Storer storer = storerMap.get(cacheKey);
        if (null == storer) {
            storer = new Storer();
            Storer previous = storerMap.putIfAbsent(cacheKey, storer);
            if (null == previous) {
                logger.debug("......create deleteId");
                StringBuilder sql = new StringBuilder();
                sql.append("<script>delete from ").append(tableName);
                sql.append(findWhereSql(queryClass));
                sql.append("</script>");
                buildMappedStatement(cacheKey, SqlCommandType.DELETE, sql.toString(), queryClass, Integer.class);
                storer.setMappedStatementId(cacheKey);
            } else {
                storer = previous;
            }
        }
        return storer.getMappedStatementId();
    }

    protected String updateByQueryId(Class<? extends RootEntity> entityClass, Class<? extends RootQuery> queryClass) {
        return updateId(entityClass, queryClass);
    }

    private String updateId(Class<? extends RootEntity> entityClass, Class<? extends RootQuery> queryClass) {
        String cacheKey = null == queryClass ? String.format("%s.%s", entityClass.getName(), "update") : String.format("%s.%s", queryClass.getName(), "update");
        Storer storer = storerMap.get(cacheKey);
        if (null == storer) {
            storer = new Storer();
            Storer previous = storerMap.putIfAbsent(cacheKey, storer);
            if (null == previous) {
                logger.debug("......create updateId");
                EntityStorer entityStorer = buildEntityStorer(entityClass);
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("<script>update ").append(entityStorer.getTableName()).append("<set>");
                entityStorer.getFields().forEach(e -> {
                    sqlBuilder.append("<if test=\"@liangchen.wang.gradf.framework.data.GradfOgnl@isNotEmpty(entity.").append(e).append(")\">");
                    sqlBuilder.append(e).append("=#{entity.").append(e).append("},");
                    sqlBuilder.append("</if>");
                });

                sqlBuilder.append("<if test=\"@liangchen.wang.gradf.framework.data.GradfOgnl@isNotEmpty(entity.dynamicFields)\">");
                sqlBuilder.append("<foreach collection=\"entity.dynamicFields.keys\" item=\"key\" separator=\",\">");
                sqlBuilder.append("<if test=\"@liangchen.wang.gradf.framework.data.GradfOgnl@isNull(entity.dynamicFields[key])\">");
                sqlBuilder.append("${key} = null");
                sqlBuilder.append("</if>");
                sqlBuilder.append("<if test=\"@liangchen.wang.gradf.framework.data.GradfOgnl@isNotNull(entity.dynamicFields[key])\">");
                sqlBuilder.append("${key} = #{entity.dynamicFields.${key}}");
                sqlBuilder.append("</if>");
                sqlBuilder.append("</foreach>");
                sqlBuilder.append("</if>");
                sqlBuilder.append("</set>");
                sqlBuilder.append(findWhereSql(queryClass));
                sqlBuilder.append("</script>");
                String sql = sqlBuilder.toString();
                buildMappedStatement(cacheKey, SqlCommandType.UPDATE, sql, entityClass, Integer.class);
                storer.setMappedStatementId(cacheKey);
            } else {
                storer = previous;
            }
        }
        return storer.getMappedStatementId();
    }

    protected String countId(Class<? extends RootQuery> queryClass) {
        String tableName = tableNameByQueryClass(queryClass);
        String cacheKey = String.format("%s.%s", queryClass.getName(), "count");
        Storer storer = storerMap.get(cacheKey);
        if (null == storer) {
            storer = new Storer();
            Storer previous = storerMap.putIfAbsent(cacheKey, storer);
            if (null == previous) {
                logger.debug("......create countId");
                StringBuilder countSql = new StringBuilder();
                // 根据mysql文档，count(0)和count(*)没有实现及性能上的差别,但count(*)符合标准语法
                // count(column)只计数非null
                countSql.append("<script>select count(*) from ").append(tableName);
                countSql.append(findWhereSql(queryClass));
                countSql.append("</script>");
                buildMappedStatement(cacheKey, SqlCommandType.SELECT, countSql.toString(), queryClass, Integer.class);
                storer.setMappedStatementId(cacheKey);
            } else {
                storer = previous;
            }
        }
        return storer.getMappedStatementId();
    }

    protected String listId(Class<? extends RootQuery> queryClass, Class<? extends RootEntity> entityClass) {
        String cacheKey = String.format("%s.%s", entityClass.getName(), "list");
        Storer storer = storerMap.get(cacheKey);
        if (null == storer) {
            storer = new Storer();
            Storer previous = storerMap.putIfAbsent(cacheKey, storer);
            if (null == previous) {
                logger.debug("......create listId");
                EntityStorer entityStorer = buildEntityStorer(entityClass);
                StringBuilder listSql = new StringBuilder();
                listSql.append("<script>select <trim suffixOverrides=\",\"><foreach collection=\"returnFields\" item=\"item\" index=\"index\" separator=\",\">${item}</foreach></trim> from ").append(entityStorer.getTableName());
                listSql.append(findWhereSql(queryClass));
                listSql.append("<if test=\"true==forUpdate\">").append("for update").append("</if>");
                listSql.append("<if test=\"@liangchen.wang.gradf.framework.data.GradfOgnl@isNotEmpty(orderBy)\"> order by <foreach collection=\"orderBy\" item=\"item\" index=\"index\" separator=\",\"> ${item.orderBy} ${item.direction} </foreach></if>");
                listSql.append("<if test=\"null!=offset and null!=rows\">limit #{offset},#{rows}</if>");
                listSql.append("</script>");
                buildMappedStatement(cacheKey, SqlCommandType.SELECT, listSql.toString(), queryClass, entityClass);
                storer.setMappedStatementId(cacheKey);
            } else {
                storer = previous;
            }
        }
        return storer.getMappedStatementId();
    }

    private StringBuilder findWhereSql(Class<? extends RootQuery> queryClass) {
        StringBuilder whereSql = new StringBuilder();
        whereSql.append("<where>");
        Field[] fields = queryClass.getDeclaredFields();
        for (Field field : fields) {
            Query annotation = field.getAnnotation(Query.class);
            if (null == annotation) {
                continue;
            }
            String fieldName = field.getName();
            String columnName = annotation.column();
            if (columnName.length() == 0) {
                columnName = fieldName;
            }
            whereSql.append("<if test=\"@liangchen.wang.gradf.framework.data.GradfOgnl@isNotEmpty(").append(fieldName).append(")\">");
            AndOr andOr = annotation.andOr();
            whereSql.append(andOr.getAndOr()).append(columnName);
            Operator operator = annotation.operator();
            switch (operator) {
                case ISNULL:
                case ISNOTNULL:
                    whereSql.append(operator.getOperator());
                    break;
                case BETWEEN:
                case NOTBETWEEN:
                    Class<?> fieldType = field.getType();
                    if (fieldType.isAssignableFrom(Between.class)) {
                        whereSql.append(operator.getOperator()).append("#{").append(fieldName).append(".min}").append(" and #{").append(fieldName).append(".max}");
                        break;
                    }
                    throw new InfoException("使用操作符:{},字段:{}类型必须为Between", operator, fieldName);
                case EQUALS:
                case NOTEQUALS:
                    whereSql.append(operator.getOperator()).append("#{").append(fieldName).append("}");
                    break;
                case GREATERTHAN:
                case GREATERTHAN_OR_EQUALS:
                case LESSTHAN:
                case LESSTHAN_OR_EQUALS:
                    whereSql.append(" <![CDATA[").append(operator.getOperator()).append("]]> ").append("#{").append(fieldName).append("}");
                    break;
                case STARTWITH:
                case NOTSTARTWITH:
                    whereSql.append("<bind name=\"_").append(fieldName).append("\" value=\"_parameter." + fieldName + "+'%'\"/>");
                    whereSql.append(operator.getOperator()).append("#{").append("_").append(fieldName).append("}");
                    break;
                case ENDWITH:
                case NOTENDWITH:
                    whereSql.append("<bind name=\"_").append(fieldName).append("\" value=\"'%'+_parameter.").append(fieldName).append("\"/>");
                    whereSql.append(operator.getOperator()).append("#{").append("_").append(fieldName).append("}");
                    break;
                case CONTAINS:
                case NOTCONTAINS:
                    whereSql.append("<bind name=\"_").append(fieldName).append("\" value=\"'%'+_parameter.").append(fieldName).append("+'%'\"/>");
                    whereSql.append(operator.getOperator()).append("#{").append("_").append(fieldName).append("}");
                    break;
                case IN:
                case NOTIN:
                    fieldType = field.getType();
                    if (fieldType.isArray() || fieldType.isAssignableFrom(List.class) || fieldType.isAssignableFrom(Set.class)) {
                        whereSql.append(operator.getOperator()).append("<foreach open=\"(\" close=\")\" collection=\"").append(fieldName).append("\" item=\"item\" index=\"index\" separator=\",\">#{item}</foreach>");
                        break;
                    }
                    throw new InfoException("使用操作符:{},字段:{}类型必须为Array、List、Set", operator, fieldName);
                default:
                    throw new InfoException("不支持的运算符:{}", operator.getOperator());
            }
            whereSql.append("</if>");
        }
        whereSql.append("</where>");
        return whereSql;
    }

    private String tableNameByQueryClass(Class<? extends RootQuery> queryClass) {
        Table table = queryClass.getAnnotation(Table.class);
        Assert.INSTANCE.notNull(table, "Query类必须使用注解@javax.persistence.Table(name=\"\")指定数据库表");
        String tableName = table.name();
        Assert.INSTANCE.notBlank(tableName, "Query类必须使用注解@javax.persistence.Table(name=\"\")指定数据库表");
        return tableName;
    }

    private EntityStorer buildEntityStorer(Class<?> entityClass) {
        Metamodel metamodel = entityManager.getMetamodel();
        EntityTypeImpl entityType = (EntityTypeImpl) metamodel.entity(entityClass);
        String tableName = entityType.getName();
        Set<?> attributes = entityType.getAttributes();
        Map<Boolean, Set<String>> fieldMap = attributes.stream().map(e -> (SingularAttribute) e).collect(Collectors.groupingBy(SingularAttribute::isId, Collectors.mapping(SingularAttribute::getName, Collectors.toSet())));
        return new EntityStorer(tableName, fieldMap.get(Boolean.TRUE), fieldMap.get(Boolean.FALSE));
    }


    private void buildMappedStatement(String mappedStatementId, SqlCommandType sqlCommandType, String sql, Class<?> parameterType, Class<?> resultType) {
        List<ResultMap> resultMaps = new ArrayList<>(1);
        Configuration configuration = sqlSessionTemplate.getConfiguration();
        resultMaps.add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, Collections.emptyList()).build());
        SqlSource sqlSource;
        if (null == parameterType) {
            sqlSource = new StaticSqlSource(configuration, sql);
        } else {
            LanguageDriver languageDriver = configuration.getDefaultScriptingLanguageInstance();
            sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
        }
        MappedStatement ms = new MappedStatement.Builder(configuration, mappedStatementId, sqlSource, sqlCommandType).resultMaps(resultMaps).build();
        configuration.addMappedStatement(ms);
    }


    private final class Storer {
        private String mappedStatementId;

        public <R> R execute(Function<String, R> function) {
            while (null == mappedStatementId) {
                ConcurrentUtil.INSTANCE.threadSleep(1, TimeUnit.MICROSECONDS);
            }
            return function.apply(mappedStatementId);
        }

        public String getMappedStatementId() {
            while (null == mappedStatementId) {
                ConcurrentUtil.INSTANCE.threadSleep(1, TimeUnit.MICROSECONDS);
            }
            return mappedStatementId;
        }

        public void setMappedStatementId(String mappedStatementId) {
            this.mappedStatementId = mappedStatementId;
        }
    }

    private final class EntityStorer {
        private final String tableName;
        private final Set<String> pks;
        private final Set<String> fields;

        private EntityStorer(String tableName, Set<String> pks, Set<String> fields) {
            this.tableName = tableName;
            this.pks = null == pks ? Collections.emptySet() : pks;
            this.fields = null == fields ? Collections.emptySet() : fields;
        }

        public String getTableName() {
            return tableName;
        }

        public Set<String> getPks() {
            return pks;
        }

        public Set<String> getFields() {
            return fields;
        }
    }
}