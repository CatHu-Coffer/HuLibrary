package com.example.catl.hulibarry.sqlite.dialect;


import com.example.catl.hulibarry.sqlite.exception.DatabaseException;
import com.example.catl.hulibarry.sqlite.meta.MetaData;
import com.example.catl.hulibarry.sqlite.meta.Sql;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnMeta;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnType;
import com.example.catl.hulibarry.sqlite.util.reflect.ReflectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tanqimin on 2015/11/3.
 */
public abstract class DefaultDialect
        implements IDialect {

    protected final static Map<String, String> CLAUSE_CACHE = new HashMap<>();

    protected final Pattern selectPattern       = Pattern.compile("^\\s*SELECT\\s+", Pattern.CASE_INSENSITIVE);
    protected final Pattern orderPattern        = Pattern.compile("\\s+ORDER\\s+BY\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    protected final Pattern groupPattern        = Pattern.compile("\\s+GROUP\\s+BY\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    protected final Pattern havingPattern       = Pattern.compile("\\s+HAVING\\s+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    protected final Pattern selectSinglePattern = Pattern
            .compile("^\\s*SELECT\\s+((COUNT)\\([\\s\\S]*\\)\\s*,?)+((\\s*)|(\\s+FROM[\\s\\S]*))?$", Pattern.CASE_INSENSITIVE);

    @Override
    public abstract String getDialectName();

    @Override
    public <TModel> Sql insert(
            Class<TModel> clazz,
            TModel model) {
        StringBuilder sql      = new StringBuilder("INSERT INTO ").append(getTableName(clazz)).append(" (");
        StringBuilder paramSql = new StringBuilder(" VALUES (");
        List<Object> params   = new ArrayList<>();

        List<ColumnMeta> columns = MetaData.columns(clazz, ColumnType.WRITABLE);
        for (ColumnMeta column : columns) {
            sql.append(column.getColumnName()).append(",");
            paramSql.append("?,");
            params.add(ReflectUtil.getFieldValue(model, column.getFieldName()));
        }
        sql.deleteCharAt(sql.lastIndexOf(",")).append(")");
        paramSql.deleteCharAt(paramSql.lastIndexOf(",")).append(")");
        return new Sql(sql.append(paramSql).toString(), params);
    }

    @Override
    public <TModel> String insert(
            Class<TModel> clazz,
            List<ColumnMeta> columns) {
        StringBuilder sql      = new StringBuilder("INSERT INTO ").append(getTableName(clazz)).append(" (");
        StringBuilder paramSql = new StringBuilder(" VALUES (");
        for (ColumnMeta column : columns) {
            sql.append(column.getColumnName()).append(",");
            paramSql.append("?,");
        }
        sql.deleteCharAt(sql.lastIndexOf(",")).append(")");
        paramSql.deleteCharAt(paramSql.lastIndexOf(",")).append(")");
        return sql.append(paramSql).toString();
    }

    @Override
    public <TModel> Sql update(
            Class<TModel> clazz,
            TModel model) {
        Sql    sql;
        Object param;

        sql = new Sql(String.format("UPDATE %s", getTableName(clazz))).append("SET");
        List<ColumnMeta> columns = MetaData.columns(clazz, ColumnType.WRITABLE);

        try {
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i).getIsPrimaryKey()) {
                    continue;
                }
                if (i == columns.size() - 1) {
                    param = ReflectUtil.getGetter(clazz, columns.get(i).getFieldName()).invoke(model);
                    sql.append(String.format("%s = ?", columns.get(i).getColumnName()), param);
                } else {
                    param = ReflectUtil.getGetter(clazz, columns.get(i).getFieldName()).invoke(model);
                    sql.append(String.format("%s = ?,", columns.get(i).getColumnName()), param);
                }

            }

        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        return sql.where(whereById(model));
    }

    @Override
    public <TModel> Sql delete(
            Class<TModel> clazz,
            TModel model) {
        return new Sql().delete(getTableName(clazz)).where(whereById(model));
    }

    @Override
    public <TModel> Sql deleteById(
            Class<TModel> clazz,
            Object primaryKey) {
        return new Sql().delete(getTableName(clazz)).where(String.format("%s = ?", MetaData.getPrimaryKey(clazz).getColumnName()), primaryKey);
    }

    @Override
    public <TModel> Sql deleteByIds(
            Class<TModel> clazz,
            Object[] primaryKeys) {
        Sql sql = new Sql().delete(getTableName(clazz));
        if (primaryKeys.length == 0) {
            throw new DatabaseException("请传入删除参数。");
        }
        return sql.where(Sql.In(MetaData.getPrimaryKey(clazz).getColumnName(), primaryKeys));
    }

    @Override
    public <TModel> Sql selectById(
            Class<TModel> clazz,
            Object id) {
        return select(clazz).append(String.format("WHERE %s = ?", MetaData.getPrimaryKey(clazz).getColumnName()), id);
    }

    @Override
    public <TModel> Sql selectByIds(
            Class<TModel> clazz,
            Object[] primaryKeys) {
        return select(clazz).where(Sql.In(MetaData.getPrimaryKey(clazz).getColumnName(), primaryKeys));
    }

    @Override
    public Sql count(
            String sql,
            Object[] params) {
        Matcher om = orderPattern.matcher(sql);
        if (om.find()) {
            sql = sql.substring(0, om.start());
/*            int index = om.end();
            if (index > sql.lastIndexOf(")")) {
                sql = sql.substring(0, om.start());
            }*/
        }
        return new Sql("SELECT COUNT(*) FROM (" + sql + ") count_alias", params);
    }

    @Override
    public <TModel> Sql countBy(
            Class<TModel> clazz,
            String where,
            Object[] params) {
        return new Sql(String.format("SELECT COUNT(*) FROM %s", getTableName(clazz))).where(where, params);
    }

    @Override
    public <TModel> Sql select(Class<TModel> clazz) {
        return new Sql(String.format("SELECT * FROM %s", getTableName(clazz)));
    }

    protected static <TModel> String getTableName(Class<TModel> clazz) {
        return MetaData.table(clazz).getTableName();
    }

    private <TModel> Sql whereById(TModel model) {
        ColumnMeta primaryKey = MetaData.getPrimaryKey(model.getClass());
        return new Sql(String.format("%s = ?", primaryKey.getColumnName()), ReflectUtil.getFieldValue(model, primaryKey.getFieldName()));
    }
}
