package liangchen.wang.gradf.framework.data.manager.impl;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.condition.JdbcConditionAnnotation;
import liangchen.wang.gradf.framework.data.dao.IDesignerDao;
import liangchen.wang.gradf.framework.data.domain.DesignerDomain;
import liangchen.wang.gradf.framework.data.domain.DesignerParameterDomain;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;
import liangchen.wang.gradf.framework.data.entity.Column;
import liangchen.wang.gradf.framework.data.entity.Columns;
import liangchen.wang.gradf.framework.data.manager.IDesignerManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang 2019/11/20 12:34
 */
@JdbcConditionAnnotation
@Component("Gradf_Data_DesignerManager")
public class DesignerManagerImpl implements IDesignerManager {
    private final IDesignerDao dao;

    @Inject
    public DesignerManagerImpl(IDesignerDao dao) {
        this.dao = dao;
    }

    @Override
    public void columns(DesignerParameterDomain parameter) {
        Assert.INSTANCE.validate(parameter);
        Columns columns = dao.columns(parameter.getTableName());
        DesignerDomain designerDomain = createDesignerData(parameter.getTableName(), parameter.getEntityName(), parameter.getModuleName(), parameter.getExportPath(), parameter.getMainPackage(), columns);
        designerDomain.setAuthor(parameter.getAuthor());
        createEntity(designerDomain, columns, parameter.isExtendedBaseEntity());
        String statusField = parameter.getStatusField();
        String statusFieldClassName = parameter.getStatusFieldClassName();
        if (parameter.isExtendedBaseEntity()) {
            statusField = "status";
            statusFieldClassName = "String";
        }
        createQuery(designerDomain, columns, statusFieldClassName, statusField);
        createIDao(designerDomain, statusFieldClassName, statusField);
        createMybatis(designerDomain);
        createDao(designerDomain, statusFieldClassName, statusField);
        createParameterDomain(designerDomain, columns, parameter.isExtendedBaseEntity());
        createResultDomain(designerDomain, columns);
        createIManager(designerDomain, columns, statusFieldClassName, statusField);
        designerDomain.setBusinessName(parameter.getBusinessName());
        designerDomain.setBusinessType(parameter.getBusinessType());
        createManager(designerDomain, columns, statusFieldClassName, statusField);
    }

    private DesignerDomain createDesignerData(String tableName, String entityName, String moduleName, String exportPath, String mainPackage, Columns columns) {
        //构造需要的类
        DesignerDomain designerDomain = new DesignerDomain();
        designerDomain.setTableName(tableName);
        //idao
        String iDaoPackage = mainPackage + ".dao";
        String iDaoPath = exportPath + File.separator + iDaoPackage.replaceAll("\\.", "\\" + File.separator);
        String iDaoName = String.format("I%sDao", entityName);
        String iDaoClass = iDaoPackage + "." + iDaoName;
        designerDomain.setIDaoPackage(iDaoPackage);
        designerDomain.setIDaoPath(iDaoPath);
        designerDomain.setIDaoName(iDaoName);
        designerDomain.setIDaoClass(iDaoClass);

        //entity
        String entityPackage = iDaoPackage + ".entity";
        String entityPath = exportPath + File.separator + entityPackage.replaceAll("\\.", "\\" + File.separator);
        String entityClass = entityPackage + "." + entityName;
        designerDomain.setEntityPackage(entityPackage);
        designerDomain.setEntityPath(entityPath);
        designerDomain.setEntityClass(entityClass);
        designerDomain.setEntityName(entityName);
        //query
        String queryPackage = iDaoPackage + ".query";
        String queryPath = exportPath + File.separator + queryPackage.replaceAll("\\.", "\\" + File.separator);
        String queryName = entityName + "Query";
        String queryClass = queryPackage + "." + queryName;
        designerDomain.setQueryPackage(queryPackage);
        designerDomain.setQueryPath(queryPath);
        designerDomain.setQueryClass(queryClass);
        designerDomain.setQueryName(queryName);
        //mapper
        String mapperPackage = iDaoPackage + ".mapper";
        String mapperPath = exportPath + File.separator + mapperPackage.replaceAll("\\.", "\\" + File.separator);
        String mapperName = moduleName + "_" + entityName + ".mapper.xml";
        designerDomain.setMapperPath(mapperPath);
        designerDomain.setMapperName(mapperName);
        //daoImpl
        String daoPackage = iDaoPackage + ".impl";
        String daoPath = exportPath + File.separator + daoPackage.replaceAll("\\.", "\\" + File.separator);
        String daoName = String.format("%sDaoImpl", entityName);
        String daoClass = daoPackage + "." + daoName;
        String daoBeanName = moduleName + "_Default" + entityName + "Dao";
        designerDomain.setDaoPackage(daoPackage);
        designerDomain.setDaoPath(daoPath);
        designerDomain.setDaoName(daoName);
        designerDomain.setDaoClass(daoClass);
        designerDomain.setDaoBeanName(daoBeanName);

        //imanage
        String iManagerPackage = mainPackage + ".manager";
        String iManagerPath = exportPath + File.separator + iManagerPackage.replaceAll("\\.", "\\" + File.separator);
        String iManagerName = String.format("I%sManager", entityName);
        String iManagerClass = iManagerPackage + "." + iManagerName;
        designerDomain.setIManagerPackage(iManagerPackage);
        designerDomain.setIManagerPath(iManagerPath);
        designerDomain.setIManagerName(iManagerName);
        designerDomain.setIManagerClass(iManagerClass);

        String domainPackage = iManagerPackage + ".domain";
        //parameterDomain
        String parameterDomainPackage = domainPackage + ".parameter";
        String parameterDomainPath = exportPath + File.separator + parameterDomainPackage.replaceAll("\\.", "\\" + File.separator);
        String parameterDomainName = String.format("%sParameterDomain", entityName);
        String parameterDomainClass = parameterDomainPackage + "." + parameterDomainName;
        designerDomain.setParameterDomainPackage(parameterDomainPackage);
        designerDomain.setParameterDomainPath(parameterDomainPath);
        designerDomain.setParameterDomainName(parameterDomainName);
        designerDomain.setParameterDomainClass(parameterDomainClass);
        //resultDaomain
        String resultDomainPackage = domainPackage + ".result";
        String resultDomainPath = exportPath + File.separator + resultDomainPackage.replaceAll("\\.", "\\" + File.separator);
        String resultDomainName = String.format("%sResultDomain", entityName);
        String resultDomainClass = resultDomainPackage + "." + resultDomainName;
        designerDomain.setResultDomainPackage(resultDomainPackage);
        designerDomain.setResultDomainPath(resultDomainPath);
        designerDomain.setResultDomainName(resultDomainName);
        designerDomain.setResultDomainClass(resultDomainClass);
        //managerImpl
        String managerImplPackage = iManagerPackage + ".impl";
        String managerImplPath = exportPath + File.separator + managerImplPackage.replaceAll("\\.", "\\" + File.separator);
        String managerImplName = String.format("%sManagerImpl", entityName);
        String managerImplClass = managerImplPackage + "." + managerImplName;
        designerDomain.setManagerImplPackage(managerImplPackage);
        designerDomain.setManagerImplPath(managerImplPath);
        designerDomain.setManagerImplName(managerImplName);
        designerDomain.setManagerImplClass(managerImplClass);
        String managerBeanName = moduleName + "_Default" + entityName + "Manager";
        designerDomain.setManagerBeanName(managerBeanName);

        //pk
        String primaryKeyName;
        String primaryKeyClass;
        List<Column> primaryKeys = columns.getPrimaryKeys();
        if (1 == primaryKeys.size()) {
            primaryKeyName = primaryKeys.get(0).getJavaTypeName();
            primaryKeyClass = primaryKeyName.toLowerCase();
        } else {
            primaryKeyName = entityName;
            primaryKeyClass = entityClass;
        }
        designerDomain.setPrimaryKeyName(primaryKeyName);
        designerDomain.setPrimaryKeyClass(primaryKeyClass);

        return designerDomain;
    }

    private void createEntity(DesignerDomain designerDomain, Columns columns, boolean extendedBaseEntity) {
        String author = designerDomain.getAuthor();
        String entityPackage = designerDomain.getEntityPackage();
        String tableName = designerDomain.getTableName();
        String entityPath = designerDomain.getEntityPath();
        String entityName = designerDomain.getEntityName();
        Collection<String> lines = new ArrayList<String>();
        lines.add("package " + entityPackage + ";");
        lines.add("");
        lines.add("import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;");
        if (extendedBaseEntity) {
            lines.add("import liangchen.wang.gradf.framework.data.entity.BaseEntity;");
        } else {
            lines.add("import liangchen.wang.gradf.framework.data.core.RootEntity;");
        }
        lines.add("");
        lines.add("import javax.persistence.Entity;");
        lines.add("import javax.persistence.Id;");
        lines.add("");
        lines.add("/**");
        lines.add(" * @author " + author + " " + DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS());
        lines.add("*/");
        lines.add("@Entity(name = \"" + tableName + "\")");
        if (extendedBaseEntity) {
            lines.add("public class " + entityName + " extends BaseEntity {");
        } else {
            lines.add("public class " + entityName + " extends RootEntity {");
        }
        lines.add("    private static final " + entityName + " self = new " + entityName + "();");
        lines.add("");
        lines.add("    public static " + entityName + " newInstance() {");
        lines.add("        return ClassBeanUtil.INSTANCE.cast(self.clone());");
        lines.add("    }");
        lines.add("");
        List<Column> primaryKeys = columns.getPrimaryKeys();
        primaryKeys.stream().forEach(e -> {
            lines.add("    @Id");
            lines.add("    private " + e.getJavaTypeName() + " " + e.getColumnName() + ";");
        });
        List<Column> fields = columns.getColumns();
        if (extendedBaseEntity) {
            Field[] baseEntityFields = BaseEntity.class.getDeclaredFields();
            List<String> fieldNames = Arrays.stream(baseEntityFields).map(Field::getName).collect(Collectors.toList());
            fields = fields.stream().filter(e -> !fieldNames.contains(e.getColumnName())).collect(Collectors.toList());
        }
        fields.stream().forEach(e -> {
            lines.add("    private " + e.getJavaTypeName() + " " + e.getColumnName() + ";");
        });
        lines.add("");
        primaryKeys.stream().forEach(e -> {
            lines.add("    public " + e.getJavaTypeName() + " get" + StringUtils.capitalize(e.getColumnName()) + "() {");
            lines.add("        return " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(e.getColumnName()) + "(" + e.getJavaTypeName() + " " + e.getColumnName() + ") {");
            lines.add("        this." + e.getColumnName() + " = " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
        });
        fields.stream().forEach(e -> {
            lines.add("    public " + e.getJavaTypeName() + " get" + StringUtils.capitalize(e.getColumnName()) + "() {");
            lines.add("        return " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(e.getColumnName()) + "(" + e.getJavaTypeName() + " " + e.getColumnName() + ") {");
            lines.add("        this." + e.getColumnName() + " = " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
        });
        lines.add("}");

        File entityFile = new File(entityPath + File.separator + entityName + ".java");
        try {
            FileUtils.writeLines(entityFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    private void createQuery(DesignerDomain designerDomain, Columns columns, String statusFieldClassName, String statusField) {
        String author = designerDomain.getAuthor();
        String tableName = designerDomain.getTableName();
        String queryPackage = designerDomain.getQueryPackage();
        String queryPath = designerDomain.getQueryPath();
        String queryName = designerDomain.getQueryName();

        Collection<String> lines = new ArrayList<String>();
        lines.add("package " + queryPackage + ";");
        lines.add("");
        lines.add("import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;");
        lines.add("import liangchen.wang.gradf.framework.data.annotation.Query;");
        lines.add("import liangchen.wang.gradf.framework.data.core.RootQuery;");
        lines.add("import liangchen.wang.gradf.framework.data.enumeration.Operator;");
        lines.add("");
        lines.add("import javax.persistence.Table;");
        lines.add("");
        lines.add("/**");
        lines.add(" * @author " + author + " " + DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS());
        lines.add("*/");
        lines.add("@Table(name = \"" + tableName + "\")");
        lines.add("public class " + queryName + " extends RootQuery {");
        lines.add("    private static final " + queryName + " self = new " + queryName + "();");
        lines.add("");
        lines.add("    public static " + queryName + " newInstance() {");
        lines.add("        return ClassBeanUtil.INSTANCE.cast(self.clone());");
        lines.add("    }");
        lines.add("");
        String primaryKeyParameter = columns.getPrimaryKeys().stream().map(e -> e.getJavaTypeName() + " " + e.getColumnName()).collect(Collectors.joining(", "));
        lines.add("    public static " + queryName + " newInstance(" + primaryKeyParameter + ") {");
        lines.add("        " + queryName + " query = newInstance();");
        columns.getPrimaryKeys().forEach(pk -> lines.add("        query.set" + StringUtils.capitalize(pk.getColumnName()) + "(" + pk.getColumnName() + ");"));
        lines.add("        return query;");
        lines.add("    }");
        lines.add("");
        List<Column> primaryKeys = columns.getPrimaryKeys();
        primaryKeys.stream().forEach(e -> {
            lines.add("    @Query(operator = Operator.EQUALS, column = \"" + e.getColumnName() + "\")");
            lines.add("    private " + e.getJavaTypeName() + " " + e.getColumnName() + ";");
        });
        lines.add("");
        if (StringUtil.INSTANCE.isNotBlank(statusFieldClassName) && StringUtil.INSTANCE.isNotBlank(statusField)) {
            lines.add("    @Query(operator = Operator.EQUALS, column = \"" + statusField + "\")");
            lines.add("    private " + statusFieldClassName + " " + statusField + ";");
            lines.add("    @Query(operator = Operator.IN, column = \"" + statusField + "\")");
            lines.add("    private " + statusFieldClassName + "[] " + statusField + "In;");
            lines.add("    @Query(operator = Operator.NOTIN, column = \"" + statusField + "\")");
            lines.add("    private " + statusFieldClassName + "[] " + statusField + "NotIn;");
        }
        lines.add("");

        primaryKeys.stream().forEach(e -> {
            String javaTypeName = e.getJavaTypeName();
            String columnName = e.getColumnName();
            lines.add("    public " + javaTypeName + " get" + StringUtils.capitalize(columnName) + "() {");
            lines.add("        return " + columnName + ";");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(columnName) + "(" + javaTypeName + " " + columnName + ") {");
            lines.add("        this." + columnName + " = " + columnName + ";");
            lines.add("    }");
            lines.add("");
        });
        if (StringUtil.INSTANCE.isNotBlank(statusFieldClassName) && StringUtil.INSTANCE.isNotBlank(statusField)) {
            lines.add("    public " + statusFieldClassName + " get" + StringUtils.capitalize(statusField) + "() {");
            lines.add("        return " + statusField + ";");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(statusField) + "(" + statusFieldClassName + " " + statusField + ") {");
            lines.add("        this." + statusField + " = " + statusField + ";");
            lines.add("    }");
            lines.add("");
            //IN
            lines.add("    public " + statusFieldClassName + "[] get" + StringUtils.capitalize(statusField) + "In() {");
            lines.add("        return " + statusField + "In;");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(statusField) + "In(" + statusFieldClassName + "[] " + statusField + "In) {");
            lines.add("        this." + statusField + "In = " + statusField + "In;");
            lines.add("    }");
            lines.add("");
            //NOTIN
            lines.add("    public " + statusFieldClassName + "[] get" + StringUtils.capitalize(statusField) + "NotIn() {");
            lines.add("        return " + statusField + "NotIn;");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(statusField) + "NotIn(" + statusFieldClassName + "[] " + statusField + "NotIn) {");
            lines.add("        this." + statusField + "NotIn = " + statusField + "NotIn;");
            lines.add("    }");
        }
        lines.add("}");

        File entityFile = new File(queryPath + File.separator + queryName + ".java");
        try {
            FileUtils.writeLines(entityFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    private void createIDao(DesignerDomain designerDomain, String statusFieldClassName, String statusField) {
        String author = designerDomain.getAuthor();
        String iDaoPackage = designerDomain.getIDaoPackage();
        String entityClass = designerDomain.getEntityClass();
        String entityName = designerDomain.getEntityName();
        String queryClass = designerDomain.getQueryClass();
        String queryName = designerDomain.getQueryName();
        String iDaoName = designerDomain.getIDaoName();
        String iDaoPath = designerDomain.getIDaoPath();
        Collection<String> lines = new ArrayList<>();
        lines.add("package " + iDaoPackage + ";");
        lines.add("");

        lines.add("import " + entityClass + ";");
        lines.add("import " + queryClass + ";");
        lines.add("import liangchen.wang.gradf.framework.data.base.IAbstractDao;");
        lines.add("");
        lines.add("/**");
        lines.add(" * @author " + author + " " + DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS());
        lines.add("*/");
        lines.add("public interface " + iDaoName + " extends IAbstractDao<" + entityName + "," + queryName + "> {");
        lines.add("");
        lines.add("}");
        File iDaoFile = new File(iDaoPath + File.separator + iDaoName + ".java");
        try {
            FileUtils.writeLines(iDaoFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    private void createMybatis(DesignerDomain designerDomain) {
        String mapperName = designerDomain.getMapperName();
        String mapperPath = designerDomain.getMapperPath();
        String iDaoClass = designerDomain.getIDaoClass();
        Collection<String> lines = new ArrayList<>();
        lines.add("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        lines.add("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        lines.add("<mapper namespace=\"" + iDaoClass + "\">");
        lines.add("");
        lines.add("</mapper>");

        File mybatisFile = new File(mapperPath + File.separator + mapperName);
        try {
            FileUtils.writeLines(mybatisFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    private void createDao(DesignerDomain designerDomain, String statusFieldClassName, String statusField) {
        String author = designerDomain.getAuthor();
        String daoPackage = designerDomain.getDaoPackage();
        String iDaoClass = designerDomain.getIDaoClass();
        String entityClass = designerDomain.getEntityClass();
        String daoBeanName = designerDomain.getDaoBeanName();
        String daoName = designerDomain.getDaoName();
        String entityName = designerDomain.getEntityName();
        String queryName = designerDomain.getQueryName();
        String queryClass = designerDomain.getQueryClass();
        String iDaoName = designerDomain.getIDaoName();
        String daoPath = designerDomain.getDaoPath();

        Collection<String> lines = new ArrayList<>();
        lines.add("package " + daoPackage + ";");
        lines.add("");
        lines.add("import " + iDaoClass + ";");
        lines.add("import " + entityClass + ";");
        lines.add("import " + queryClass + ";");
        lines.add("");
        lines.add("import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;");
        lines.add("import org.springframework.stereotype.Repository;");
        lines.add("");
        lines.add("import java.util.concurrent.TimeUnit;");

        //lines.add("import " + queryClass + ";");
        lines.add("");
        lines.add("/**");
        lines.add(" * @author " + author + " " + DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS());
        lines.add("*/");
        lines.add("@Repository(\"" + daoBeanName + "\")");
        lines.add("@GradfAutoCacheable(clearMethods = {\"insert*\", \"delete*\", \"update*\"}, durationRange = \"1-24\", timeUnit = TimeUnit.HOURS)");
        lines.add("public class " + daoName + " extends AbstractJdbcDao<" + entityName + "," + queryName + "> implements " + iDaoName + " {");
        lines.add("");
        lines.add("}");

        File daoFile = new File(daoPath + File.separator + daoName + ".java");
        try {
            FileUtils.writeLines(daoFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    private void createParameterDomain(DesignerDomain designerDomain, Columns columns, boolean extendedBaseEntity) {
        String author = designerDomain.getAuthor();
        String parameterDomainPackage = designerDomain.getParameterDomainPackage();
        String parameterDomainName = designerDomain.getParameterDomainName();
        String parameterDomainPath = designerDomain.getParameterDomainPath();
        String entityName = designerDomain.getEntityName();
        String entityClass = designerDomain.getEntityClass();
        Collection<String> lines = new ArrayList<>();
        lines.add("package " + parameterDomainPackage + ";");
        lines.add("");
        lines.add("import " + entityClass + ";");
        lines.add("import liangchen.wang.gradf.component.commons.base.ParameterDomain;");
        lines.add("import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;");
        lines.add("");
        lines.add("/**");
        lines.add(" * @author " + author + " " + DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS());
        lines.add("*/");
        lines.add("public class " + parameterDomainName + " extends ParameterDomain<" + entityName + "> {");
        lines.add("    private static final " + parameterDomainName + " self = new " + parameterDomainName + "();");
        lines.add("");
        lines.add("    public static " + parameterDomainName + " newInstance() {");
        lines.add("        return ClassBeanUtil.INSTANCE.cast(self.clone());");
        lines.add("    }");
        lines.add("");
        List<Column> primaryKeys = columns.getPrimaryKeys();
        primaryKeys.stream().forEach(e -> {
            lines.add("    private " + e.getJavaTypeName() + " " + e.getColumnName() + ";");
        });
        List<Column> fields = columns.getColumns();
        if (extendedBaseEntity) {
            Field[] baseEntityFields = BaseEntity.class.getDeclaredFields();
            List<String> fieldNames = Arrays.stream(baseEntityFields).map(Field::getName).collect(Collectors.toList());
            fieldNames.removeIf(e -> e.equals("sort") || e.equals("summary") || e.equals("status"));
            fields = fields.stream().filter(e -> !fieldNames.contains(e.getColumnName())).collect(Collectors.toList());
        }
        fields.stream().forEach(e -> {
            lines.add("    private " + e.getJavaTypeName() + " " + e.getColumnName() + ";");
        });
        lines.add("");
        primaryKeys.stream().forEach(e -> {
            lines.add("    public " + e.getJavaTypeName() + " get" + StringUtils.capitalize(e.getColumnName()) + "() {");
            lines.add("        return " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(e.getColumnName()) + "(" + e.getJavaTypeName() + " " + e.getColumnName() + ") {");
            lines.add("        this." + e.getColumnName() + " = " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
        });
        fields.stream().forEach(e -> {
            lines.add("    public " + e.getJavaTypeName() + " get" + StringUtils.capitalize(e.getColumnName()) + "() {");
            lines.add("        return " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(e.getColumnName()) + "(" + e.getJavaTypeName() + " " + e.getColumnName() + ") {");
            lines.add("        this." + e.getColumnName() + " = " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
        });
        lines.add("}");

        File parameterDomainFile = new File(parameterDomainPath + File.separator + parameterDomainName + ".java");
        try {
            FileUtils.writeLines(parameterDomainFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    private void createResultDomain(DesignerDomain designerDomain, Columns columns) {
        String author = designerDomain.getAuthor();
        String resultDomainPackage = designerDomain.getResultDomainPackage();
        String resultDomainName = designerDomain.getResultDomainName();
        String resultDomainPath = designerDomain.getResultDomainPath();
        Collection<String> lines = new ArrayList<String>();
        lines.add("package " + resultDomainPackage + ";");
        lines.add("");
        lines.add("import liangchen.wang.gradf.component.commons.base.ResultDomain;");
        lines.add("import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;");
        lines.add("");
        lines.add("/**");
        lines.add(" * @author " + author + " " + DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS());
        lines.add("*/");
        lines.add("public class " + resultDomainName + " extends ResultDomain {");
        lines.add("    private static final " + resultDomainName + " self = new " + resultDomainName + "();");
        lines.add("");
        lines.add("    public static " + resultDomainName + " newInstance() {");
        lines.add("        return ClassBeanUtil.INSTANCE.cast(self.clone());");
        lines.add("    }");
        lines.add("");
        List<Column> primaryKeys = columns.getPrimaryKeys();
        primaryKeys.stream().forEach(e -> {
            lines.add("    private " + e.getJavaTypeName() + " " + e.getColumnName() + ";");
        });
        List<Column> fields = columns.getColumns();
        fields.stream().forEach(e -> {
            lines.add("    private " + e.getJavaTypeName() + " " + e.getColumnName() + ";");
        });
        lines.add("");
        //拼合主键列
        fields.addAll(0, primaryKeys);
        fields.stream().forEach(e -> {
            lines.add("    public " + e.getJavaTypeName() + " get" + StringUtils.capitalize(e.getColumnName()) + "() {");
            lines.add("        return " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
            lines.add("    public void set" + StringUtils.capitalize(e.getColumnName()) + "(" + e.getJavaTypeName() + " " + e.getColumnName() + ") {");
            lines.add("        this." + e.getColumnName() + " = " + e.getColumnName() + ";");
            lines.add("    }");
            lines.add("");
        });
        lines.add("}");

        File resultDomainFile = new File(resultDomainPath + File.separator + resultDomainName + ".java");
        try {
            FileUtils.writeLines(resultDomainFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    private void createIManager(DesignerDomain designerDomain, Columns columns, String statusFieldClassName, String statusField) {
        String author = designerDomain.getAuthor();
        String iManagerPackage = designerDomain.getIManagerPackage();
        String queryClass = designerDomain.getQueryClass();
        String queryName = designerDomain.getQueryName();
        String parameterDomainClass = designerDomain.getParameterDomainClass();
        String resultDomainClass = designerDomain.getResultDomainClass();
        String resultDomainName = designerDomain.getResultDomainName();
        String parameterDomainName = designerDomain.getParameterDomainName();
        String iManagerName = designerDomain.getIManagerName();
        String iManagerPath = designerDomain.getIManagerPath();
        Collection<String> lines = new ArrayList<>();
        lines.add("package " + iManagerPackage + ";");
        lines.add("");
        lines.add("import " + queryClass + ";");
        lines.add("import " + parameterDomainClass + ";");
        lines.add("import " + resultDomainClass + ";");
        lines.add("import liangchen.wang.gradf.framework.data.pagination.PaginationResult;");
        lines.add("");
        lines.add("import java.util.List;");
        lines.add("");
        lines.add("/**");
        lines.add(" * @author " + author + " " + DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS());
        lines.add("*/");
        lines.add("public interface " + iManagerName + "  {");
        lines.add("");
        lines.add("    boolean insert(" + parameterDomainName + " parameter);");
        lines.add("");
        String primaryKeyParameter = columns.getPrimaryKeys().stream().map(e -> e.getJavaTypeName() + " " + e.getColumnName()).collect(Collectors.joining(", "));
        lines.add("    boolean deleteByPrimaryKey(" + primaryKeyParameter + ");");
        lines.add("");
        lines.add("    boolean updateByPrimaryKey(" + parameterDomainName + " parameter);");
        lines.add("");
        lines.add("    int updateByQuery(" + parameterDomainName + " parameter, " + queryName + " query);");
        lines.add("");
        if (StringUtil.INSTANCE.isBlank(statusField) || StringUtil.INSTANCE.isBlank(statusFieldClassName)) {
            lines.add("    " + resultDomainName + " byPrimaryKey(" + primaryKeyParameter + ");");
            lines.add("");
            lines.add("    " + resultDomainName + " byPrimaryKeyOrThrow(" + primaryKeyParameter + ");");
            lines.add("");
        } else {
            lines.add("    boolean updateStatusByPrimaryKey(" + primaryKeyParameter + ", " + statusFieldClassName + " statusTo, " + statusFieldClassName + "... statusFrom);");
            lines.add("");
            lines.add("    boolean updateStatusByPrimaryKey(" + primaryKeyParameter + ", " + statusFieldClassName + " statusTo, " + statusFieldClassName + "[] statusIn, " + statusFieldClassName + "[] statusNotIn);");
            lines.add("");
            lines.add("    " + resultDomainName + " byPrimaryKey(" + primaryKeyParameter + ", String... returnFields);");
            lines.add("");
            lines.add("    " + resultDomainName + " byPrimaryKeyOrThrow(" + primaryKeyParameter + ", String... returnFields);");
            lines.add("");
            lines.add("    " + resultDomainName + " byPrimaryKeyOrThrow(" + primaryKeyParameter + ", " + statusFieldClassName + "[] statusIn, " + statusFieldClassName + "[] statusNotIn, String... returnFields);");
            lines.add("");
            lines.add("    " + resultDomainName + " byPrimaryKey(" + primaryKeyParameter + ", " + statusFieldClassName + "[] statusIn, " + statusFieldClassName + "[] statusNotIn, String... returnFields);");
            lines.add("");
        }
        lines.add("    List<" + resultDomainName + "> list(" + queryName + " query, String... returnFields);");
        lines.add("");
        lines.add("    PaginationResult<" + resultDomainName + "> pagination(" + queryName + " query, String... returnFields);");
        lines.add("");
        lines.add("}");
        File iManagerFile = new File(iManagerPath + File.separator + iManagerName + ".java");
        try {
            FileUtils.writeLines(iManagerFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    private void createManager(DesignerDomain designerDomain, Columns columns, String statusFieldClassName, String statusField) {
        String author = designerDomain.getAuthor();
        String managerPackage = designerDomain.getManagerImplPackage();
        String iDaoClass = designerDomain.getIDaoClass();
        String iDaoName = designerDomain.getIDaoName();
        String queryClass = designerDomain.getQueryClass();
        String queryName = designerDomain.getQueryName();
        String entityClass = designerDomain.getEntityClass();
        String entityName = designerDomain.getEntityName();
        String iManagerClass = designerDomain.getIManagerClass();
        String iManagerName = designerDomain.getIManagerName();
        String parameterDomainClass = designerDomain.getParameterDomainClass();
        String resultDomainClass = designerDomain.getResultDomainClass();
        String resultDomainName = designerDomain.getResultDomainName();
        String parameterDomainName = designerDomain.getParameterDomainName();
        String managerBeanName = designerDomain.getManagerBeanName();
        String daoBeanName = designerDomain.getDaoBeanName();
        String managerName = designerDomain.getManagerImplName();
        String managerPath = designerDomain.getManagerImplPath();
        Collection<String> lines = new ArrayList<>();
        lines.add("package " + managerPackage + ";");
        lines.add("");
        lines.add("import liangchen.wang.gradf.component.commons.base.AbstractManager;");
        lines.add("import liangchen.wang.gradf.framework.data.pagination.PaginationResult;");
        lines.add("import liangchen.wang.gradf.framework.data.enumeration.Status;");
        lines.add("import liangchen.wang.gradf.framework.commons.validator.Assert;");
        lines.add("import liangchen.wang.gradf.framework.commons.utils.ContextUtil;");
        lines.add("import " + iDaoClass + ";");
        lines.add("import " + entityClass + ";");
        lines.add("import " + queryClass + ";");
        lines.add("import " + iManagerClass + ";");
        lines.add("import " + parameterDomainClass + ";");
        lines.add("import " + resultDomainClass + ";");
        lines.add("import org.springframework.stereotype.Component;");
        lines.add("");
        lines.add("import javax.inject.Inject;");
        lines.add("import javax.inject.Named;");
        lines.add("import java.util.List;");
        lines.add("import java.time.LocalDateTime;");
        lines.add("");
        lines.add("/**");
        lines.add(" * @author " + author + " " + DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS());
        lines.add("*/");
        lines.add("@Component(\"" + managerBeanName + "\")");
        lines.add("public class " + managerName + " extends AbstractManager<" + entityName + ", " + queryName + ", " + resultDomainName + "> implements " + iManagerName + " {");
        lines.add("    @Inject");
        lines.add("    public " + managerName + "(@Named(\"" + daoBeanName + "\") " + iDaoName + " dao) {");
        lines.add("        super(\"" + designerDomain.getBusinessName() + "\", \"" + designerDomain.getBusinessType() + "\", dao);");
        lines.add("    }");
        lines.add("");
        lines.add("    @Override");
        lines.add("    public boolean insert(" + parameterDomainName + " parameter) {");
        lines.add("        Assert.INSTANCE.notNull(parameter, \"参数不能为空\");");
        List<Column> primaryKeys = columns.getPrimaryKeys();
        if (primaryKeys.size() == 1) {
            String columnName = primaryKeys.get(0).getColumnName();
            String suffix = StringUtils.capitalize(columnName);
            lines.add("    Assert.INSTANCE.notNull(parameter.get" + suffix + "(), () -> parameter.set" + suffix + "(UidDb.INSTANCE.uid()));");
        }
        if (StringUtil.INSTANCE.isNotBlank(statusField) && "String".equals(statusFieldClassName)) {
            String suffix = StringUtils.capitalize(statusField);
            lines.add("            Assert.INSTANCE.notBlank(parameter.get" + suffix + "(), () -> parameter.set" + suffix + "(Status.NORMAL.name()));");
        }
        String entityNameVar = StringUtil.INSTANCE.firstLetterConvertCase(entityName);
        lines.add("        parameter.populateEntity((" + entityNameVar + ") -> {");
        lines.add("            //TODO 这里可以对Entity补充数据");
        lines.add("        });");
        lines.add("        return super.insert(parameter);");
        lines.add("    }");
        lines.add("");
        String primaryKeyParameter = primaryKeys.stream().map(e -> e.getJavaTypeName() + " " + e.getColumnName()).collect(Collectors.joining(", "));
        String primaryKeyVar = primaryKeys.stream().map(e -> e.getColumnName()).collect(Collectors.joining(", "));
        lines.add("    @Override");
        lines.add("    public boolean deleteByPrimaryKey(" + primaryKeyParameter + ") {");
        lines.add("        return updateStatusByPrimaryKey(" + primaryKeyVar + ", Status.DELETED.name());");
        lines.add("    }");
        lines.add("");
        lines.add("    @Override");
        lines.add("    public boolean updateByPrimaryKey(" + parameterDomainName + " parameter) {");
        lines.add("         Assert.INSTANCE.notNull(parameter, \"参数不能为空\");");
        lines.add("        " + queryName + " query = " + queryName + ".newInstance();");
        primaryKeys.forEach(pk -> lines.add("        query.set" + StringUtils.capitalize(pk.getColumnName()) + "(parameter.get" + StringUtils.capitalize(pk.getColumnName()) + "());"));
        lines.add("        int rows =  updateByQuery(parameter, query);");
        lines.add("        return rows == 1;");
        lines.add("    }");
        lines.add("");
        lines.add("    @Override");
        lines.add("    public int updateByQuery(" + parameterDomainName + " parameter, " + queryName + " query) {");
        lines.add("        Assert.INSTANCE.notNull(parameter, \"参数不能为空\");");
        lines.add("        Assert.INSTANCE.notNull(query, \"查询参数不能为空\");");
        lines.add("        parameter.populateEntity((" + entityNameVar + ") -> {");
        lines.add("            // TODO 这里添加不更新或者不论是否空值总更新的字段");
        lines.add("            " + entityNameVar + ".setModify_datetime(LocalDateTime.now());");
        lines.add("            " + entityNameVar + ".setModifier(FouraUtil.INSTANCE.getOperator());");
        lines.add("        });");
        lines.add("        return super.updateByQuery(parameter, query);");
        lines.add("    }");
        lines.add("");
        if (StringUtil.INSTANCE.isBlank(statusField) || StringUtil.INSTANCE.isBlank(statusFieldClassName)) {
            lines.add("    @Override");
            lines.add("    public " + resultDomainName + " byPrimaryKey(" + primaryKeyParameter + ") {");
            for (String p : primaryKeyVar.split(",")) {
                lines.add("        Assert.INSTANCE.notBlank(" + p + ", \"参数不能为空\");");
            }
            lines.add("        " + queryName + " query = " + queryName + ".newInstance();");
            primaryKeys.forEach(pk -> lines.add("        query.set" + StringUtils.capitalize(pk.getColumnName()) + "(" + pk.getColumnName() + ");"));
            lines.add("        return one(query);");
            lines.add("    }");
            lines.add("");
            lines.add("    @Override");
            lines.add("    public " + resultDomainName + " byPrimaryKeyOrThrow(" + primaryKeyParameter + ") {");
            for (String p : primaryKeyVar.split(",")) {
                lines.add("        Assert.INSTANCE.notBlank(" + p + ", \"参数不能为空\");");
            }
            lines.add("        " + queryName + " query = " + queryName + ".newInstance();");
            primaryKeys.forEach(pk -> lines.add("        query.set" + StringUtils.capitalize(pk.getColumnName()) + "(" + pk.getColumnName() + ");"));
            lines.add("        " + resultDomainName + " result = byPrimaryKey(" + primaryKeyVar + ");");
            lines.add("        Assert.INSTANCE.notNull(result, \"数据不存在\");");
            lines.add("        return result;");
            lines.add("    }");
            lines.add("");
        } else {
            lines.add("    @Override");
            lines.add("    public boolean updateStatusByPrimaryKey(" + primaryKeyParameter + ", " + statusFieldClassName + " statusTo, " + statusFieldClassName + "... statusFrom) {");
            lines.add("        return updateStatusByPrimaryKey(" + primaryKeyVar + ", statusTo, statusFrom, null);");
            lines.add("    }");
            lines.add("");
            lines.add("    @Override");
            lines.add("    public boolean updateStatusByPrimaryKey(" + primaryKeyParameter + ", " + statusFieldClassName + " statusTo, " + statusFieldClassName + "[] statusIn, " + statusFieldClassName + "[] statusNotIn) {");
            for (String p : primaryKeyVar.split(",")) {
                lines.add("        Assert.INSTANCE.notNull(" + p + ", \"参数不能为空\");");
            }
            lines.add("        Assert.INSTANCE.notBlank(statusTo, \"状态不能为空\");");
            lines.add("        " + parameterDomainName + " parameter = " + parameterDomainName + ".newInstance();");
            lines.add("        parameter.setStatus(statusTo);");
            lines.add("        " + queryName + " query = " + queryName + ".newInstance();");
            primaryKeys.forEach(pk -> lines.add("        query.set" + StringUtils.capitalize(pk.getColumnName()) + "(" + pk.getColumnName() + ");"));
            lines.add("        query.setStatusIn(statusIn);");
            lines.add("        query.setStatusNotIn(statusNotIn);");
            lines.add("        int rows = super.updateByQuery(parameter, query);");
            lines.add("        return rows == 1;");
            lines.add("    }");
            lines.add("");
            lines.add("    @Override");
            lines.add("    public " + resultDomainName + " byPrimaryKey(" + primaryKeyParameter + ", String... returnFields) {");
            lines.add("        return byPrimaryKey(" + primaryKeyVar + ", null, null, returnFields);");
            lines.add("    }");
            lines.add("");
            lines.add("    @Override");
            lines.add("    public " + resultDomainName + " byPrimaryKeyOrThrow(" + primaryKeyParameter + ", String... returnFields) {");
            lines.add("        return byPrimaryKeyOrThrow(" + primaryKeyVar + ", null, null, returnFields);");
            lines.add("    }");
            lines.add("");
            lines.add("    @Override");
            lines.add("    public " + resultDomainName + " byPrimaryKeyOrThrow(" + primaryKeyParameter + ", " + statusFieldClassName + "[] statusIn, " + statusFieldClassName + "[] statusNotIn, String... returnFields) {");
            lines.add("        " + resultDomainName + " resultDomain = byPrimaryKey(" + primaryKeyVar + ", statusIn, statusNotIn, returnFields);");
            lines.add("        Assert.INSTANCE.notNull(resultDomain, \"数据不存在或者状态错误\");");
            lines.add("        return resultDomain;");
            lines.add("    }");
            lines.add("");
            lines.add("    @Override");
            lines.add("    public " + resultDomainName + " byPrimaryKey(" + primaryKeyParameter + ", " + statusFieldClassName + "[] statusIn, " + statusFieldClassName + "[] statusNotIn, String... returnFields) {");
            for (String p : primaryKeyVar.split(",")) {
                lines.add("        Assert.INSTANCE.notNull(" + p + ", \"参数不能为空\");");
            }
            lines.add("        " + queryName + " query = " + queryName + ".newInstance();");
            primaryKeys.forEach(pk -> lines.add("        query.set" + StringUtils.capitalize(pk.getColumnName()) + "(" + pk.getColumnName() + ");"));
            lines.add("        query.setStatusIn(statusIn);");
            lines.add("        query.setStatusNotIn(statusNotIn);");
            lines.add("        return one(query, returnFields);");
            lines.add("    }");
            lines.add("");
        }
        lines.add("    @Override");
        lines.add("    public List<" + resultDomainName + "> list(" + queryName + " query, String... returnFields) {");
        lines.add("        return super.list(query, returnFields);");
        lines.add("    }");
        lines.add("");
        lines.add("    @Override");
        lines.add("    public PaginationResult<" + resultDomainName + "> pagination(" + queryName + " query, String... returnFields) {");
        lines.add("        return super.pagination(query, returnFields);");
        lines.add("    }");
        lines.add("");
        lines.add("}");
        File managerFile = new File(managerPath + File.separator + managerName + ".java");
        try {
            FileUtils.writeLines(managerFile, "utf-8", lines);
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }
}
