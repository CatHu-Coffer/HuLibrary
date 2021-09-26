package com.example.catl.hulibarry.sqlite.meta;


import com.example.catl.hulibarry.sqlite.exception.DatabaseException;
import com.example.catl.hulibarry.sqlite.util.StringUtil;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by tanqimin on 2015/9/16.
 */
public class Sql {
    public static boolean       showSql   = false;
    private StringBuilder sql       = new StringBuilder();
    private List<Object> params    = new ArrayList<>();
    private       boolean       completed = false;

    /**
     * 构造方法
     */
    public Sql() {
    }

    /**
     * 构造方法
     *
     * @param sql
     */
    public Sql(String sql) {
        this.sql = new StringBuilder(sql);
    }

    /**
     * 构造方法
     *
     * @param sql
     * @param params
     */
    public Sql(
            String sql,
            List<Object> params) {
        this(sql);
        this.params.addAll(params);
    }

    /**
     * 构造方法
     *
     * @param sql
     * @param params
     */
    public Sql(
            String sql,
            Object... params) {
        this(sql, Arrays.asList(params));
    }

    /**
     * 构造方法
     *
     * @param sql
     */
    public Sql(Sql sql) {
        this(sql.getSql(), sql.getParams());
    }

    public static Sql SQL() {
        return new Sql();
    }

    /**
     * 创建 “字段 LIKE 参数值”
     *
     * @param field
     * @param param
     * @return
     */
    public static Sql Like(
            String field,
            String param) {
        return SQL().like(field, param);
    }

    /**
     * 创建 “字段 = 参数值”
     *
     * @param field
     * @param param
     * @return
     */
    public static Sql Eq(
            String field,
            Object param) {
        return SQL().eq(field, param);
    }

    /**
     * 创建 “字段 = 参数值” ，如果参数为NULL时，返回 “字段 IS NULL”
     *
     * @param field
     * @param param
     * @return
     */
    public static Sql EqWithNull(
            String field,
            Object param) {
        return SQL().eqWithNull(field, param);
    }

    /**
     * 创建 “字段 != 参数值”
     *
     * @param field
     * @param param
     * @return
     */
    public static Sql Uq(
            String field,
            Object param) {
        return SQL().uq(field, param);
    }

    /**
     * 创建 “字段 IS NULL”
     *
     * @param field
     * @return
     */
    public static Sql IsNull(String field) {
        return SQL().isNull(field);
    }

    /**
     * 创建 “字段 IS NOT NULL”
     *
     * @param field
     * @return
     */
    public static Sql IsNotNull(String field) {
        return SQL().isNotNull(field);
    }

    /**
     * 创建 “字段 > 参数值”
     *
     * @param field
     * @param param
     * @return
     */
    public static Sql Gt(
            String field,
            Object param) {
        return SQL().gt(field, param);
    }

    /**
     * 创建 “字段 >= 参数值”
     *
     * @param field
     * @param param
     * @return
     */
    public static Sql Ge(
            String field,
            Object param) {
        return SQL().ge(field, param);
    }

    /**
     * 创建 “字段 < 参数值”
     *
     * @param field
     * @param param
     * @return
     */
    public static Sql Lt(
            String field,
            Object param) {
        return SQL().lt(field, param);
    }

    /**
     * 创建 “字段 <= 参数值”
     *
     * @param field
     * @param param
     * @return
     */
    public static Sql Le(
            String field,
            Object param) {
        return SQL().le(field, param);
    }

    /**
     * 创建 “字段 BETWEEN 参数值1 AND 参数值2”
     * 如果参数值1为NULL时，创建 “字段 <= 参数值2”
     * 如果参数值2为NULL时，创建 “字段 >= 参数值1”
     *
     * @param field
     * @param start
     * @param end
     * @return
     */
    public static Sql Between(
            String field,
            Object start,
            Object end) {
        return SQL().between(field, start, end);
    }

    /**
     * 创建 “字段 IN (参数值1, 参数值2, ..., 参数值N)”
     *
     * @param field
     * @param params
     * @return
     */
    public static Sql In(
            String field,
            Object... params) {
        return SQL().in(field, Arrays.asList(params), EmptyParamEnum.FETCH_ALL);
    }

    /**
     * 创建 “字段 IN (参数值1, 参数值2, ..., 参数值N)”
     *
     * @param field
     * @param params
     * @return
     */
    public static Sql In(
            String field,
            Collection params) {
        return SQL().in(field, params, EmptyParamEnum.FETCH_ALL);
    }

    /**
     * 创建 “字段 IN (参数值1, 参数值2, ..., 参数值N)”
     *
     * @param field
     * @param params
     * @param emptyParamEnum
     * @return
     */
    public static Sql In(
            String field,
            Collection params,
            EmptyParamEnum emptyParamEnum) {
        return SQL().in(field, params, emptyParamEnum);
    }

    /**
     * 创建 “字段 IN (子查询)”
     *
     * @param field
     * @param subSql
     * @return
     */
    public static Sql In(
            String field,
            Sql subSql) {
        return SQL().in(field, subSql);
    }

    /**
     * 创建 “字段 NOT IN (参数值1, 参数值2, ..., 参数值N)”
     *
     * @param field
     * @param params
     * @return
     */
    public static Sql NotIn(
            String field,
            Object... params) {
        return SQL().notIn(field, params);
    }

    /**
     * 创建 “字段 IN (参数值1, 参数值2, ..., 参数值N)”
     *
     * @param field
     * @param params
     * @return
     */
    public static Sql NotIn(
            String field,
            Collection params) {
        return SQL().NotIn(field, params);
    }


    /**
     * 创建 “字段 NOT IN (子查询)”
     *
     * @param field
     * @param subSql
     * @return
     */
    public static Sql NotIn(
            String field,
            Sql subSql) {
        return SQL().notIn(field, subSql);
    }

    /**
     * 创建 “EXISTS (子查询)”
     *
     * @param subSql
     * @return
     */
    public static Sql Exists(Sql subSql) {
        return SQL().exists(subSql);
    }

    /**
     * 创建 “NOT EXISTS (子查询)”
     *
     * @param subSql
     * @return
     */
    public static Sql NotExists(Sql subSql) {
        return SQL().notExists(subSql);
    }

    /**
     * 创建 “AND (条件)”
     *
     * @param conditionSql
     * @return
     */
    public static Sql And(Sql conditionSql) {
        return SQL().and(conditionSql);
    }

    /**
     * 创建 “OR (条件)”
     *
     * @param conditionSql
     * @return
     */
    public static Sql Or(Sql conditionSql) {
        return SQL().or(conditionSql);
    }

    /**
     * 创建 “SELECT 字段”
     *
     * @param field
     * @return
     */
    public static Sql Select(String field) {
        return SQL().select(field);
    }

    /**
     * 创建 “INSERT 数据表 (字段)”
     *
     * @param table
     * @param fields
     * @return
     */
    public static Sql Insert(
            String table,
            String fields) {
        return SQL().insert(table, fields);
    }

    /**
     * 创建 “UPDATE 数据表”
     *
     * @param table
     * @return
     */
    public static Sql Update(String table) {
        return SQL().update(table);
    }

    /**
     * 创建 “DELETE FROM 数据表”
     *
     * @param table
     * @return
     */
    public static Sql Delete(String table) {
        return SQL().delete(table);
    }

    /**
     * 创建 “DELETE 数据表别名 FROM 数据表 数据表别名”
     *
     * @param table
     * @param alias
     * @return
     */
    public static Sql Delete(
            String table,
            String alias) {
        return SQL().delete(table, alias);
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * 已完成
     */
    public void completed() {
        completed = true;
    }

    public void checkCompleted() {
        if (completed) {
            throw new DatabaseException("SQL 语句标记为已完成状态，不允许修改！");
        }
    }

    /**
     * 把当前SQL作为子查询
     * SELECT * FROM ( [当前SQL] ) AS alias
     *
     * @param alias
     * @return
     */
    public Sql asSubQuery(String alias) {
        checkCompleted();
        this.sql.insert(0, "SELECT * FROM ( ").append(String.format(" ) AS %s", alias));
        return this;
    }

    /**
     * 在当前SQL后拼接语句
     *
     * @param clause
     * @return
     */
    public Sql append(String clause) {
        return this.append(clause, new Object[]{});
    }

    /**
     * 在当前SQL后拼接语句
     *
     * @param clause
     * @param param
     * @return
     */
    public Sql append(
            String clause,
            Object param) {
        return append(clause, new Object[]{param});
    }

    /**
     * 在当前SQL后拼接语句
     *
     * @param clause
     * @param param
     * @return
     */
    public Sql append(
            String clause,
            Collection param) {
        return append(clause, param.toArray());
    }


    /**
     * 在当前SQL后拼接语句
     *
     * @param clause
     * @param params
     * @return
     */
    public Sql append(
            String clause,
            Object... params) {
        checkCompleted();
        if (StringUtil.startWithSpace(clause) || this.sql.length() == 0) {
            this.sql.append(clause);
        } else {
            this.sql.append(String.format(" %s", clause));
        }

        this.addParams(params);
        return this;
    }

    /**
     * 在当前SQL后拼接语句
     *
     * @param sql
     * @return
     */
    public Sql append(Sql sql) {
        return append(sql.getSql(), sql.getParams());
    }

    /**
     * 在当前SQL后拼接语句,并换行
     *
     * @param clause
     * @return
     */
    public Sql appendLine(String clause) {
        return this.append(clause).wrap();
    }

    /**
     * 在当前SQL后拼接语句,并换行
     *
     * @param clause
     * @param param
     * @return
     */
    public Sql appendLine(
            String clause,
            Object param) {
        return this.append(clause, param).wrap();
    }

    /**
     * 在当前SQL后拼接语句,并换行
     *
     * @param clause
     * @param params
     * @return
     */
    public Sql appendLine(
            String clause,
            Object... params) {
        return this.append(clause, params).wrap();
    }

   /* *//**
     * 在当前SQL后拼接语句,并换行
     *
     * @param clause
     * @param params
     * @return
     *//*
    public Sql appendLine(
            String clause,
            Collection... params) {
        return this.append(clause, params).wrap();
    }*/

    /**
     * 在当前SQL后拼接语句
     *
     * @param sql
     * @return
     */
    public Sql appendLine(Sql sql) {
        return this.appendLine(sql.getSql(), sql.getParams());
    }

    /**
     * 换行
     *
     * @return
     */
    public Sql wrap() {
        return this.append("\n");
    }

    /**
     * 拼接 “SELECT 字段”
     *
     * @param field
     * @return
     */
    public Sql select(String field) {
        return this.append(String.format("SELECT %s", ValidateUtil.isBlank(field) ? "*" : field));
    }

    /**
     * 拼接 “FROM 数据表”
     *
     * @param table
     * @return
     */
    public Sql from(String table) {
        return this.from(table, "");
    }

    /**
     * 拼接 “FROM 数据表 数据表别名”
     *
     * @param table
     * @param alias
     * @return
     */
    public Sql from(
            String table,
            String alias) {
        return this.deleteLastChar(",").append(String.format("FROM %s %s", table, alias));
    }


    /**
     * 拼接 “LEFT OUTER”
     *
     * @return
     */
    public Sql left() {
        return this.append("LEFT OUTER");
    }

    /**
     * 拼接 “RIGHT OUTER”
     *
     * @return
     */
    public Sql right() {
        return this.append("RIGHT OUTER");
    }

    /**
     * 拼接 “INNER”
     *
     * @return
     */
    public Sql inner() {
        return this.append("INNER");
    }

    /**
     * 拼接 “FULL”
     *
     * @return
     */
    public Sql full() {
        return this.append("FULL");
    }

    /**
     * 拼接 “JOIN 数据表 数据表别名”
     *
     * @param table
     * @param alias
     * @return
     */
    public Sql join(
            String table,
            String alias) {
        return this.append(String.format("JOIN %s %s", table, alias));
    }

    /**
     * 拼接 “ON 条件表达式”
     *
     * @param expression
     * @return
     */
    public Sql on(String expression) {
        return this.append(String.format("ON %s", expression));
    }

    /**
     * 拼接 “WHERE 1 = 1”
     *
     * @return
     */
    public Sql where() {
        return this.deleteLastChar(",").append("WHERE 1 = 1");
    }

    /**
     * 拼接 “WHERE 条件表达式”
     *
     * @param expression
     * @return
     */
    public Sql where(String expression) {
        return this.deleteLastChar(",").append(String.format("WHERE %s", expression));
    }

    /**
     * 拼接 “WHERE 条件表达式”
     *
     * @param expression
     * @param param
     * @return
     */
    public Sql where(
            String expression,
            Object... param) {
        return where(new Sql(expression, param));
    }

    /**
     * 拼接 “WHERE 条件表达式”
     *
     * @param sql
     * @return
     */
    public Sql where(Sql sql) {
        return this.deleteLastChar(",").append(String.format("WHERE %s", sql.getSql()), sql.getParams());
    }

    /**
     * 等于
     *
     * @param field
     * @param param
     * @return
     */
    private Sql eq(
            String field,
            Object param) {
        if (ValidateUtil.isBlank(param)) {
            return this;
        }
        return this.append(String.format("%s = ?", field), param);
    }

    private Sql eqWithNull(
            String field,
            Object param) {
        if (param == null) {
            return this.isNull(field);
        }

        return eq(field, param);
    }

    /**
     * 不等于
     *
     * @param field
     * @param param
     * @return
     */
    private Sql uq(
            String field,
            Object param) {
        if (ValidateUtil.isBlank(param)) {
            return this;
        }
        return this.append(String.format("%s != ?", field), param);
    }

    private Sql isNull(String field) {
        return this.append(String.format("%s IS NULL", field));
    }

    private Sql isNotNull(String field) {
        return this.append(String.format("%s IS NOT NULL", field));
    }

    /**
     * 大于
     *
     * @param field
     * @param param
     * @return
     */
    private Sql gt(
            String field,
            Object param) {
        if (ValidateUtil.isBlank(param)) {
            return this;
        }
        return this.append(String.format("%s > ?", field), param);
    }

    /**
     * 大于等于
     *
     * @param field
     * @param param
     * @return
     */
    private Sql ge(
            String field,
            Object param) {
        if (ValidateUtil.isBlank(param)) {
            return this;
        }
        return this.append(String.format("%s >= ?", field), param);
    }

    /**
     * 小于
     *
     * @param field
     * @param param
     * @return
     */
    private Sql lt(
            String field,
            Object param) {
        if (ValidateUtil.isBlank(param)) {
            return this;
        }
        return this.append(String.format("%s < ?", field), param);
    }

    /**
     * 小于等于
     *
     * @param field
     * @param param
     * @return
     */
    private Sql le(
            String field,
            Object param) {
        if (ValidateUtil.isBlank(param)) {
            return this;
        }
        return this.append(String.format("%s <= ?", field), param);
    }

    private Sql like(
            String field,
            String param) {
        checkCompleted();
        if (param == null) {
            return this;
        }
        //        return this.append(String.format("%s LIKE ?", field), "%".concat(param).concat("%"));
        if (param.contains("%") || param.contains("_")) {
            return this.append(String.format("%s LIKE ?", field), param);
        } else {
            return this.eq(field, param);
        }
    }

    private Sql between(
            String field,
            Object start,
            Object end) {
        if (start == null && end == null) {
            return this;
        }
        if (end == null) {
            return this.ge(field, start);
        }
        if (start == null) {
            return this.le(field, end);
        }

        return this.append(String.format("%s BETWEEN ? AND ?", field), start, end);
    }

    private Sql in(
            String field,
            Collection params,
            EmptyParamEnum emptyParamEnum) {
        StringBuilder paramMark = new StringBuilder();
        List sqlParams = new ArrayList<>();
        if (params != null && params.size() > 0) {
            for (Object param : params) {
                if (param == null) {
                    continue;
                }
                sqlParams.add(param);
                paramMark.append("?,");
            }
        }
        int paramCnt = sqlParams.size();
        if (paramCnt == 0) {
            if (emptyParamEnum == EmptyParamEnum.FETCH_NONE) {
                this.append("1 > 2");
            }
        } else if (paramCnt == 1) {
            this.eq(field, sqlParams.get(0));
        } else {
            StringBuilder sql = new StringBuilder(String.format("%s in (", field));
            sql.append(paramMark).deleteCharAt(sql.lastIndexOf(",")).append(")");
            this.append(sql.toString(), sqlParams);
        }
        return this;
    }

    private Sql in(
            String field,
            Sql subSql) {
        return this.append(String.format("%s IN (%s )", field, subSql.getSql()), subSql.getParams());
    }

    private Sql notIn(
            String field,
            Object... params) {
        this.append(String.format("%s NOT IN (", field));
        List sqlParams = new ArrayList<>();
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                continue;
            }
            sqlParams.add(params[i]);
            sql.append("?,");
        }
        this.deleteLastChar(",").append(")");
        return this;
    }

    private Sql notIn(
            String field,
            Sql subSql) {
        return this.append(String.format("%s NOT IN (%s )", field, subSql.getSql()), subSql.getParams());
    }

    private Sql exists(Sql subSql) {
        return this.append(String.format("EXISTS (%s )", subSql.getSql()), subSql.getParams());
    }

    private Sql notExists(Sql subSql) {
        return this.append(String.format("NOT EXISTS (%s )", subSql.getSql()), subSql.getParams());
    }

    private boolean isEmpty() {
        return this.getSql().length() == 0;
    }

    /**
     * 拼接一个 “GROUP BY 字段” 格式的语句
     *
     * @param field
     * @return
     */
    public Sql groupBy(String field) {
        return this.append(String.format("GROUP BY %s", field));
    }

    /**
     * 拼接一个 “HAVING 1 = 1” 格式的语句
     *
     * @return
     */
    public Sql having() {
        return this.append("HAVING 1 = 1");
    }

    /**
     * 拼接 “ORDER BY 字段”
     *
     * @param field
     * @return
     */
    public Sql orderBy(String field) {
        if (ValidateUtil.isNotBlank(field)) {
            return this.append(String.format("ORDER BY %s", field));
        }
        return this;
    }

    /**
     * 增加参数
     *
     * @param param
     */
    public void addParam(Object param) {
        this.params.add(param);
    }

    /**
     * 获取SQL语句
     *
     * @return
     */
    public String getSql() {
        return this.sql.toString();
    }

    /**
     * 获取SQL参数
     *
     * @return
     */
    public Object[] getParams() {
        return this.params.toArray();
    }

    /**
     * 清空 SQL语句 和 SQL参数
     */
    public void clear() {
        this.sql = new StringBuilder();
        this.params.clear();
        this.completed = false;
    }

    /**
     * 拼接 “INSERT INTO 数据表 (字段)”
     *
     * @param table
     * @param fields
     * @return
     */
    public Sql insert(
            String table,
            String fields) {
        return this.append(String.format("INSERT INTO %s ( %s )", table, fields));
    }

    /**
     * 拼接 “VALUES (参数1, 参数2, ..., 参数N)”
     *
     * @param params
     * @return
     */
    public Sql values(Object... params) {
        this.append("VALUES (");
        for (Object param : params) {
            this.append("?,", param);
        }
        return this.deleteLastChar(",").append(" )");
    }

    /**
     * 拼接 “UPDATE 数据表”
     *
     * @param table
     * @return
     */
    public Sql update(String table) {
        return this.append(String.format("UPDATE %s", table));
    }

    /**
     * 拼接 “SET 字段 = 参数值,”
     *
     * @param field
     * @param param
     * @return
     */
    public Sql set(
            String field,
            Object param) {
        if (this.sql.lastIndexOf(",") == this.sql.length() - 1) {
            return this.append(String.format("%s = ?,", field), param);
        }
        return this.append(String.format("SET %s = ?,", field), param);
    }

    /**
     * 拼接 “DELETE FROM 数据表”
     *
     * @param table
     * @return
     */
    public Sql delete(String table) {
        return this.append(String.format("DELETE FROM %s", table));
    }

    /**
     * 拼接 “DELETE 数据表别名 FROM 数据表 数据表别名”
     *
     * @param table
     * @param alias
     * @return
     */
    public Sql delete(
            String table,
            String alias) {
        return this.append(String.format("DELETE %s FROM %s %s", alias, table, alias));
    }

    /**
     * 拼接 “AND (条件表达式)”
     *
     * @param conditionSql
     * @return
     */
    public Sql and(Sql conditionSql) {
        if (conditionSql.getSql().length() == 0) {
            return this;
        }
        if (this.sql.length() > 0) {
            this.append("AND");
        }
        return this.append(String.format("(%s )", conditionSql.getSql()), conditionSql.getParams());
    }

    /**
     * 拼接 “OR (条件表达式)”
     *
     * @param conditionSql
     * @return
     */
    public Sql or(Sql conditionSql) {
        if (conditionSql.getSql().length() == 0) {
            return this;
        }
        if (this.sql.length() > 0) {
            this.append("OR");
        }
        return this.append(String.format("(%s )", conditionSql.getSql()), conditionSql.getParams());
    }

    /**
     * 增加参数
     *
     * @param params
     */
    public void addParams(Object[] params) {
        if (ValidateUtil.isBlank(params)) {
            return;
        }
        for (Object param : params) {
            this.addParam(param);
        }
    }

    /**
     * 增加参数
     *
     * @param params
     */
    public void addParams(Collection params) {
        if (ValidateUtil.isBlank(params)) {
            return;
        }
        addParams(params.toArray());
    }

    private Sql deleteLastChar(String str) {
        checkCompleted();
        if (isEmpty() == false) {
            if (this.sql.lastIndexOf(str) == this.sql.length() - 1) {
                this.sql.deleteCharAt(sql.lastIndexOf(str));
            }
        }
        return this;
    }

    /**
     * 拼接 “UNION”
     *
     * @return
     */
    public Sql union() {
        return this.append("UNION");
    }

    /**
     * 拼接 “ALL”
     *
     * @return
     */
    public Sql all() {
        return this.append("ALL");
    }
}
