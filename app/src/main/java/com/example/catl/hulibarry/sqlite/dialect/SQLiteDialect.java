package com.example.catl.hulibarry.sqlite.dialect;


import com.example.catl.hulibarry.sqlite.meta.MetaData;
import com.example.catl.hulibarry.sqlite.meta.Sql;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnMeta;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnType;
import com.example.catl.hulibarry.sqlite.util.reflect.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/3.
 */
public class SQLiteDialect
        extends DefaultDialect {
    @Override
    public String getDialectName() {
        return "sqlite";
    }

    @Override
    public <TModel> Sql insertOrUpdate(Class<TModel> clazz, TModel entity) {
        StringBuilder sql      = new StringBuilder("INSERT OR REPLACE INTO ").append(getTableName(clazz)).append(" (");
        StringBuilder paramSql = new StringBuilder(" VALUES (");
        List<Object> params   = new ArrayList<>();

        List<ColumnMeta> columns = MetaData.columns(clazz, ColumnType.WRITABLE);
        for (ColumnMeta column : columns) {
            sql.append(column.getColumnName()).append(",");
            paramSql.append("?,");
            params.add(ReflectUtil.getFieldValue(entity, column.getFieldName()));
        }
        sql.deleteCharAt(sql.lastIndexOf(",")).append(")");
        paramSql.deleteCharAt(paramSql.lastIndexOf(",")).append(")");
        return new Sql(sql.append(paramSql).toString(), params);
    }

    @Override
    public <TModel> String insertOrUpdate(Class<TModel> clazz, List<ColumnMeta> columns) {
        StringBuilder sql      = new StringBuilder("INSERT OR REPLACE INTO ").append(super.getTableName(clazz)).append(" (");
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
    public Sql selectTop(
            int currentPage,
            int recordsPerPage,
            String sql,
            Object[] params) {
        if (currentPage == 1 && recordsPerPage == 1) {
            //如果sql本身只返回一个结果
            if (selectSinglePattern.matcher(sql).find()) {
                return new Sql(sql);
            }
        }
        int offset = recordsPerPage * (currentPage - 1);
        return new Sql(sql, params).append("LIMIT ? OFFSET ?", recordsPerPage, offset);
    }
}
