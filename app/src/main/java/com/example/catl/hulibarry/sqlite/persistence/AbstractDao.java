package com.example.catl.hulibarry.sqlite.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;


import com.example.catl.hulibarry.sqlite.dialect.IDialect;
import com.example.catl.hulibarry.sqlite.dialect.SQLiteDialect;
import com.example.catl.hulibarry.sqlite.exception.DatabaseException;
import com.example.catl.hulibarry.sqlite.meta.MetaData;
import com.example.catl.hulibarry.sqlite.meta.Page;
import com.example.catl.hulibarry.sqlite.meta.Sql;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnMeta;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnType;
import com.example.catl.hulibarry.sqlite.meta.schema.TableMeta;
import com.example.catl.hulibarry.sqlite.persistence.handler.PropertyHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.PropertyHandlerFactory;
import com.example.catl.hulibarry.sqlite.util.DateFormat;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;
import com.example.catl.hulibarry.sqlite.util.reflect.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/4.
 */
abstract class AbstractDao<TModel> {
    private Class<TModel> clazz;
    private TableMeta tableMeta;
    private        boolean        enableCached;
    private static IDialect dialect;
    private SQLiteDatabase sqLiteDatabase;

    @SuppressWarnings("unchecked")
    private AbstractDao() {
        this.clazz = (Class<TModel>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.tableMeta = MetaData.table(this.clazz);
        this.enableCached = tableMeta.getCached();
    }


    public AbstractDao(SQLiteDatabase sqLiteDatabase) {
        this();
        this.sqLiteDatabase = sqLiteDatabase;
    }

    /**
     * 执行SQL语句，返回多行结果集
     *
     * @param clazz
     * @param sql
     * @param params
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> find(
            Class<TView> clazz,
            String sql,
            Object... params) {
        return find(clazz, new Sql(sql, params));
    }

    /**
     * 执行SQL语句，返回多行结果集
     *
     * @param clazz
     * @param sql
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> find(
            Class<TView> clazz,
            Sql sql) {
        return find(clazz, sql, this.enableCached);
    }

    /**
     * 执行SQL语句，返回多行结果集
     *
     * @param clazz
     * @param sql
     * @param cached
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> find(
            Class<TView> clazz,
            Sql sql,
            boolean cached) {

        Cursor cursor;
        Object[] params = sql.getParams();

        logSql(sql);
        if (ValidateUtil.isBlank(params)) {
            cursor = sqLiteDatabase.rawQuery(sql.getSql(), null);
        } else {
            int      length    = params.length;
            String[] strParams = new String[length];
            for (int i = 0; i < length; i++) {
                strParams[i] = params[i].toString();
            }

            cursor = sqLiteDatabase.rawQuery(sql.getSql(), strParams);
        }


        return toEntities(clazz, cursor);
    }

    private void logSql(Sql sql) {
        if (Sql.showSql) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SQL ：");
            stringBuilder.append(sql.getSql()).append("\n");
            stringBuilder.append("PARAMS ：");
            stringBuilder.append("{");
            if (sql.getParams().length > 0) {
                for (Object param : sql.getParams()) {
                    if (param == null) stringBuilder.append(" null,");
                    else stringBuilder.append(" '").append(param).append("',");
                }
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            }
            stringBuilder.append(" } \n");

        }
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回多行结果集
     *
     * @param where
     * @param params
     * @return
     */
    public List<TModel> findBy(
            String where,
            Object... params) {
        return findBy(new Sql(where, params));
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回多行结果集
     *
     * @param where
     * @return
     */
    public List<TModel> findBy(
            Sql where) {
        return findBy(where, this.enableCached);
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回多行结果集
     *
     * @param where
     * @return
     */
    public List<TModel> findBy(
            Sql where,
            boolean cached) {
        Sql sql = getDialect().select(clazz).where(where);
        return this.find(this.clazz, sql, cached);
    }

    /**
     * 执行SQL语句，返回指定行数的结果集
     *
     * @param clazz
     * @param top
     * @param sql
     * @param params
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> findTop(
            Class<TView> clazz,
            int top,
            String sql,
            Object... params) {
        return findTop(clazz, top, new Sql(sql, params));
    }

    /**
     * 执行SQL语句，返回指定行数的结果集
     *
     * @param clazz
     * @param top
     * @param sql
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> findTop(
            Class<TView> clazz,
            int top,
            Sql sql) {
        return findTop(clazz, top, sql, this.enableCached);
    }

    /**
     * 执行SQL语句，返回指定行数的结果集
     *
     * @param clazz
     * @param top
     * @param sql
     * @param cached
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> findTop(
            Class<TView> clazz,
            int top,
            Sql sql,
            boolean cached) {
        Sql querySql = getDialect().selectTop(1, top, sql.getSql(), sql.getParams());
        return find(clazz, querySql, cached);
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回指定行数的结果集
     *
     * @param top
     * @param where
     * @param params
     * @return
     */
    public List<TModel> findTopBy(
            int top,
            String where,
            Object... params) {
        return findTopBy(top, new Sql(where, params));
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回指定行数的结果集
     *
     * @param top
     * @param where
     * @return
     */
    public List<TModel> findTopBy(
            int top,
            Sql where) {
        return findTopBy(top, where, this.enableCached);
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回指定行数的结果集
     *
     * @param top
     * @param where
     * @param cached
     * @return
     */
    public List<TModel> findTopBy(
            int top,
            Sql where,
            boolean cached) {
        Sql sql = getDialect().select(clazz).where(where);
        return findTop(clazz, top, sql, cached);
    }

    /**
     * 执行SQL语句，返回分页结果集
     *
     * @param clazz          返回的数据类型
     * @param isPageable     是否分页
     * @param currentPage    当前页码
     * @param recordsPerPage 每页记录数
     * @param sql            SQL查询语句
     * @param params         SQL查询参数
     * @param <TView>
     * @return
     */
    protected <TView> Page<TView> findByPage(
            Class<TView> clazz,
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            String sql,
            Object... params) {
        return findByPage(clazz, isPageable, currentPage, recordsPerPage, new Sql(sql, params));
    }

    /**
     * 执行SQL语句，返回分页结果集
     *
     * @param clazz          返回的数据类型
     * @param isPageable     是否分页
     * @param currentPage    当前页码
     * @param recordsPerPage 每页记录数
     * @param sql            SQL查询语句
     * @param <TView>
     * @return
     */
    protected <TView> Page<TView> findByPage(
            Class<TView> clazz,
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            Sql sql) {
        return findByPage(clazz, isPageable, currentPage, recordsPerPage, sql, this.enableCached);
    }

    /**
     * 执行SQL语句，返回分页结果集
     *
     * @param clazz          返回的数据类型
     * @param isPageable     是否分页
     * @param currentPage    当前页码
     * @param recordsPerPage 每页记录数
     * @param sql            SQL查询语句
     * @param cached
     * @param <TView>
     * @return
     */
    protected <TView> Page<TView> findByPage(
            Class<TView> clazz,
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            Sql sql,
            boolean cached) {
        int curPage    = currentPage;
        int recPerPage = recordsPerPage;

        int totalRow;
        int totalPage;

        if (isPageable == false) {
            curPage = 1;
            recPerPage = Integer.MAX_VALUE;
        }

        Sql querySql = getDialect().selectTop(curPage, recPerPage, sql.getSql(), sql.getParams());

        List<TView> data = find(clazz, querySql, cached);

        if (isPageable == false) {
            totalRow = data.size();
            totalPage = 1;
        } else {
            Sql countSql = getDialect().count(sql.getSql(), sql.getParams());
            totalRow = count(countSql, cached);
            totalPage = totalRow / recordsPerPage;

            if (totalRow % recordsPerPage != 0) {
                totalPage++;
            }
        }

        if (isPageable && totalPage > 0 && curPage > totalPage) {
//            Logger.i(String.format("当前页数(%s)大于总页数(%s), 加载最后一页数据！", curPage, totalPage));
            curPage = totalPage;
            return findByPage(clazz, isPageable, curPage, recordsPerPage, sql, cached);
        }

        return new Page<>(data, curPage, recPerPage, totalPage, totalRow);
    }

    /**
     * 根据主键获取记录
     *
     * @param clazz 返回的数据类型
     * @param id    主键
     * @return
     */
    public TModel getById(
            Class<TModel> clazz,
            Object id) {
        return getById(clazz, id, this.enableCached);
    }

    /**
     * 根据主键获取记录
     *
     * @param clazz  返回的数据类型
     * @param id     主键
     * @param cached
     * @return
     */
    public TModel getById(
            Class<TModel> clazz,
            Object id,
            boolean cached) {
        Sql          sql     = getDialect().selectById(clazz, id);
        List<TModel> tModels = findTop(clazz, 1, sql, cached);
        return tModels.size() > 0 ? tModels.get(0) : null;
    }

    /**
     * 根据SQL语句获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param clazz   返回的数据类型
     * @param sql     SQL查询语句
     * @param params  SQL查询参数
     * @param <TView>
     * @return
     */
    protected <TView> TView get(
            Class<TView> clazz,
            String sql,
            Object... params) {
        return get(clazz, new Sql(sql, params));
    }

    /**
     * 根据SQL语句获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param clazz   返回的数据类型
     * @param sql     SQL查询语句
     * @param <TView>
     * @return
     */
    protected <TView> TView get(
            Class<TView> clazz,
            Sql sql) {
        return get(clazz, sql, this.enableCached);
    }

    /**
     * 根据SQL语句获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param clazz   返回的数据类型
     * @param sql     SQL查询语句
     * @param cached
     * @param <TView>
     * @return
     */
    protected <TView> TView get(
            Class<TView> clazz,
            Sql sql,
            boolean cached) {
        Sql         querySql = getDialect().selectTop(1, 1, sql.getSql(), sql.getParams());
        List<TView> result   = find(clazz, querySql, cached);
        return result.size() == 0 ? null : result.get(0);
    }

    /**
     * 根据WHERE条件获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param where
     * @param params
     * @return
     */
    public TModel getBy(
            String where,
            Object... params) {
        return getBy(new Sql(where, params));
    }

    /**
     * 根据WHERE条件获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param where
     * @return
     */
    public TModel getBy(
            Sql where) {
        return getBy(where, this.enableCached);
    }

    /**
     * 根据WHERE条件获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param where
     * @param cached
     * @return
     */
    public TModel getBy(
            Sql where,
            boolean cached) {
        Sql sql = getDialect().select(clazz).where(where);
        return get(clazz, sql, cached);
    }


    /**
     * 根据主键查询结果集
     *
     * @param primaryKeys
     * @param cached
     * @return
     */
    public List<TModel> findByIds(
            Collection primaryKeys,
            boolean cached) {
        if (ValidateUtil.isBlank(primaryKeys)) {
            return Collections.EMPTY_LIST;
        }
        Sql sql = getDialect().selectByIds(clazz, primaryKeys.toArray());
        return this.find(clazz, sql, cached);
    }

    /**
     * 根据主键查询结果集
     *
     * @param primaryKeys 主键集合
     * @return
     */
    public List<TModel> findByIds(
            Object... primaryKeys) {
        return findByIds(Arrays.asList(primaryKeys));
    }

    /**
     * 根据主键查询结果集
     *
     * @param primaryKeys
     * @return
     */
    public List<TModel> findByIds(
            Collection primaryKeys) {
        return findByIds(primaryKeys, this.enableCached);
    }

    /**
     * 根据SQL语句返回Object，用于执行COUNT等结果集只有一个值的查询
     *
     * @param clazz  返回的数据类型
     * @param sql    SQL查询语句
     * @param params SQL查询参数
     * @param <T>
     * @return
     */
    protected <T> T queryForObject(
            Class<T> clazz,
            String sql,
            Object... params) {
        return queryForObject(clazz, new Sql(sql, params));
    }

    /**
     * 根据SQL语句返回Object，用于执行COUNT等结果集只有一个值的查询
     *
     * @param clazz
     * @param sql
     * @param <T>
     * @return
     */
    protected <T> T queryForObject(
            Class<T> clazz,
            Sql sql) {
        return queryForObject(clazz, sql, this.enableCached);
    }

    /**
     * 根据SQL语句返回Object，用于执行COUNT等结果集只有一个值的查询
     *
     * @param clazz
     * @param sql
     * @param cached
     * @param <T>
     * @return
     */
    protected <T> T queryForObject(
            Class<T> clazz,
            Sql sql,
            boolean cached) {
        List<T> resultList = find(clazz, sql, cached);
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    /**
     * 根据SQL语句返回记录数
     *
     * @param sql    SQL查询语句
     * @param params SQL查询参数
     * @return
     */
    protected int count(
            String sql,
            Object... params) {
        return count(new Sql(sql, params));
    }

    /**
     * 根据SQL语句返回记录数
     *
     * @param sql
     * @return
     */
    protected int count(Sql sql) {
        return count(sql, this.enableCached);
    }

    /**
     * 根据SQL语句返回记录数
     *
     * @param sql
     * @param cached
     * @return
     */
    protected int count(
            Sql sql,
            boolean cached) {
        return queryForObject(Integer.class, sql, cached);
    }

    /**
     * 根据WHERE条件，返回记录数
     *
     * @param where
     * @param params
     * @return
     */
    public int countBy(
            String where,
            Object... params) {
        return countBy(new Sql(where, params));
    }

    /**
     * 根据WHERE条件，返回记录数
     *
     * @param where
     * @return
     */
    public int countBy(
            Sql where) {
        return countBy(where, this.enableCached);
    }

    /**
     * 根据WHERE条件，返回记录数
     *
     * @param where
     * @param cached
     * @return
     */
    public int countBy(
            Sql where,
            boolean cached) {
        Sql sql = getDialect().countBy(clazz, where.getSql(), where.getParams());
        return queryForObject(Integer.class, sql, cached);
    }

    /**
     * 保存
     *
     * @param clazz
     * @param tModel
     * @param <TModel>
     * @return
     */
    public <TModel> int save(
            Class<TModel> clazz,
            TModel tModel) {
        if (ValidateUtil.isBlank(tModel)) {
            return 0;
        }

        Sql sql = getDialect().insert(clazz, tModel);
        return execute(sql);
    }

    /**
     * 批量保存
     *
     * @param clazz
     * @param models
     * @param <TModel>
     * @return
     */
    public <TModel> int save(
            Class<TModel> clazz,
            Collection<TModel> models) {
        return saveOrUpdate(clazz, models, false);
    }

    /**
     * 保存
     *
     * @param clazz
     * @param tModel
     * @param <TModel>
     * @return
     */
    public <TModel> int saveOrUpdate(
            Class<TModel> clazz,
            TModel tModel) {
        if (ValidateUtil.isBlank(tModel)) {
            return 0;
        }

        Sql sql = getDialect().insertOrUpdate(clazz, tModel);
        return execute(sql);
    }

    /**
     * 批量保存或更新
     *
     * @param clazz
     * @param models
     * @param <TModel>
     * @return
     */
    public <TModel> int saveOrUpdate(
            Class<TModel> clazz,
            Collection<TModel> models) {
        return saveOrUpdate(clazz, models, true);
    }

    /**
     * 批量保存或更新
     *
     * @param clazz
     * @param models
     * @param <TModel>
     * @return
     */
    private <TModel> int saveOrUpdate(
            Class<TModel> clazz,
            Collection<TModel> models, boolean enableUpdate) {
        int result = 0;

        if (ValidateUtil.isBlank(models)) return result;

        List<ColumnMeta> columns = MetaData.columns(clazz, ColumnType.WRITABLE);
        String sql;
        if (enableUpdate) {
            sql = getDialect().insertOrUpdate(clazz, columns);
        } else {
            sql = getDialect().insert(clazz, columns);
        }

        SQLiteStatement sqLiteStatement = this.sqLiteDatabase.compileStatement(sql);
        TModel          model;
        Object[]        params          = new Object[columns.size()];
        Object param;
        try {
            for (Iterator<TModel> modelIterator = models.iterator(); modelIterator.hasNext(); ) {
                model = modelIterator.next();
                for (int i = 0; i < columns.size(); i++) {
                    param = ReflectUtil.getGetter(clazz, columns.get(i).getFieldName()).invoke(model);

                    if (param == null) {
                        sqLiteStatement.bindNull(i + 1);
                    } else {
                        if (param instanceof Date || param instanceof java.sql.Date || param instanceof Time || param instanceof Timestamp) {
                            param = DateFormat.DATE_TIME_FORMAT.format(param);
                            sqLiteStatement.bindString(i + 1, param.toString());
                        } else {
                            sqLiteStatement.bindString(i + 1, param.toString());
                        }
                    }
                    params[i] = param;
                }
                sqLiteStatement.execute();
                sqLiteStatement.clearBindings();

                logSql(new Sql(sql, params));
                result++;
            }
        } catch (Exception e) {
//            Logger.e(e.getMessage(), e);
            throw new DatabaseException(e);
        } finally {
            //释放资源
            model = null;
            columns = null;
            sql = null;
            param = null;
        }

        return result;
    }

    /**
     * 更新
     *
     * @param clazz
     * @param model
     * @param <TModel>
     * @return
     */
    public <TModel> int update(
            Class<TModel> clazz,
            TModel model) {
        if (ValidateUtil.isBlank(model)) {
            return 0;
        }
        Sql sql = getDialect().update(clazz, model);
        return execute(sql);
    }

    /**
     * 根据指定列更新
     *
     * @param clazz
     * @param model
     * @param columns
     * @param <TModel>
     * @return
     */
    public <TModel> int update(
            Class<TModel> clazz,
            TModel model,
            String columns) {
        if (ValidateUtil.isBlank(model)) {
            return 0;
        }
        List<TModel> models = new ArrayList<>();
        models.add(model);
        return update(clazz, models, columns);
    }

    /**
     * 批量更新
     *
     * @param clazz
     * @param models
     * @param <TModel>
     * @return
     */
    public <TModel> int update(
            Class<TModel> clazz,
            Collection<TModel> models) {
        return update(clazz, models, "*");
    }

    /**
     * 根据制定列批量更新
     *
     * @param clazz
     * @param models
     * @param columns
     * @param <TModel>
     * @return
     */
    public <TModel> int update(
            Class<TModel> clazz,
            Collection<TModel> models,
            String columns) {
        int result = 0;
        if (ValidateUtil.isBlank(models)) {
            return result;
        }

        String col           = columns;
        List<ColumnMeta> updateColumns = new ArrayList<>();
        List<ColumnMeta> dbColumns     = MetaData.columns(clazz, ColumnType.WRITABLE);

        if (ValidateUtil.isBlank(col) || col.equals("*")) {
            updateColumns.addAll(dbColumns);
        } else {
            for (String columnStr : col.split(",")) {
                ColumnMeta column = MetaData.getColumnByColumnName(clazz, columnStr.trim());
                if (column == null) {
                    throw new DatabaseException(String.format("校验失败：数据库字段 %s 不存在", columnStr));
                }
                updateColumns.add(column);
            }
        }

        Sql        sql        = Sql.Update(MetaData.table(clazz).getTableName()).append("SET");
        ColumnMeta primaryKey = MetaData.getPrimaryKey(clazz);

        List<Object> paramList;
        Object[]     params;
        try {
            for (TModel model : models) {
                paramList = new ArrayList<>();

                for (ColumnMeta updateColumn : updateColumns) {
                    if (updateColumn.getIsPrimaryKey()) {
                        continue;
                    }
                    if (sql.getCompleted() == false) {
                        sql.append(String.format("%s = ?,", updateColumn.getColumnName()));
                    }
                    Object param = ReflectUtil.getGetter(clazz, updateColumn.getFieldName()).invoke(model);

                    if (param != null && (param instanceof Date || param instanceof java.sql.Date || param instanceof Time || param instanceof Timestamp)) {
                        param = DateFormat.DATE_TIME_FORMAT.format(param);
                    }

                    paramList.add(param);
                }

                if (sql.getCompleted() == false) {
                    sql.where(String.format("%s = ?", primaryKey.getColumnName())).setCompleted(true);
                }

                paramList.add(ReflectUtil.getGetter(clazz, primaryKey.getFieldName()).invoke(model));//主键值

                params = convert(paramList);
                logSql(new Sql(sql.getSql(), params));
                sqLiteDatabase.execSQL(sql.getSql(), params);
                result++;
            }
        } catch (Exception e) {
//            Logger.e(e.getMessage(), e);
            throw new DatabaseException(e);
        }

        return result;
    }

    /**
     * 删除
     *
     * @param clazz
     * @param model
     * @param <TModel>
     * @return
     */
    public <TModel> int delete(
            Class<TModel> clazz,
            TModel model) {
        if (ValidateUtil.isBlank(model)) {
            return 0;
        }
        Sql sql = getDialect().delete(clazz, model);
        return execute(sql);
    }

    /**
     * 批量删除
     *
     * @param clazz
     * @param models
     * @param <TModel>
     * @return
     */
    public <TModel> int delete(
            Class<TModel> clazz,
            Collection<TModel> models) {
        if (ValidateUtil.isBlank(models)) {
            return 0;
        }
        List<Object> idList     = new ArrayList<>();
        ColumnMeta   primaryKey = MetaData.getPrimaryKey(clazz);

        try {
            for (TModel model : models) {
                idList.add(ReflectUtil.getGetter(clazz, primaryKey.getFieldName()).invoke(model));
            }
        } catch (Exception e) {
//            Logger.e(e.getMessage(), e);
            throw new DatabaseException(e);
        }

        return deleteByIds(clazz, convert(idList));
    }

    /**
     * 根据ID删除
     *
     * @param clazz
     * @param primaryKey
     * @param <TModel>
     * @return
     */
    public <TModel> int deleteById(
            Class<TModel> clazz,
            Object primaryKey) {
        if (ValidateUtil.isBlank(primaryKey)) {
            return 0;
        }
        Sql sql = getDialect().deleteById(clazz, primaryKey);
        return execute(sql);
    }

    /**
     * 根据ID集合删除
     *
     * @param clazz
     * @param primaryKeys
     * @param <TModel>
     * @return
     */
    protected <TModel> int deleteByIds(
            Class<TModel> clazz,
            Object... primaryKeys) {
        if (ValidateUtil.isBlank(primaryKeys)) {
            return 0;
        }

        Sql sql = getDialect().deleteByIds(clazz, primaryKeys);
        return execute(sql);
    }

    /**
     * 根据ID集合删除
     *
     * @param clazz
     * @param primaryKeys
     * @param <TModel>
     * @return
     */
    protected <TModel> int deleteByIds(
            Class<TModel> clazz,
            Collection primaryKeys) {
        return deleteByIds(clazz, convert(primaryKeys));
    }

    /**
     * 根据WHERE条件删除
     *
     * @param where
     * @param params
     * @return
     */
    protected int deleteBy(
            String where,
            Object... params) {
        Sql sql = new Sql().delete(MetaData.table(clazz).getTableName()).where(where, params);
        return execute(sql);
    }

    /**
     * 根据WHERE条件删除
     *
     * @param where
     * @return
     */
    protected int deleteBy(
            Sql where) {
        return deleteBy(where.getSql(), where.getParams());
    }

    /**
     * 根据字段删除
     *
     * @param clazz
     * @param fieldName
     * @param params
     * @param <TModel>
     * @return
     */
    protected <TModel> int deleteByField(
            Class<TModel> clazz,
            String fieldName,
            Object... params) {
        if (ValidateUtil.isBlank(params)) {
            return 0;
        }
        Sql sql = new Sql().delete(MetaData.table(clazz).getTableName()).where(Sql.In(fieldName, params));
        return execute(sql);
    }

    /**
     * 根据字段删除
     *
     * @param clazz
     * @param fieldName
     * @param params
     * @param <TModel>
     * @return
     */
    protected <TModel> int deleteByField(
            Class<TModel> clazz,
            String fieldName,
            Collection params) {
        return deleteByField(clazz, fieldName, convert(params));
    }

    /**
     * 执行SQL语句，返回执行行数
     *
     * @param sql
     * @param params
     * @return
     */
    public int execute(
            String sql,
            Object... params) {
        Object[] paramArrays = null;
        if (params != null && params.length > 0) {
            paramArrays = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Date || params[i] instanceof java.sql.Date || params[i] instanceof Time || params[i] instanceof Timestamp) {
                    paramArrays[i] = DateFormat.DATE_TIME_FORMAT.format(params[i]);
                } else {
                    paramArrays[i] = params[i];
                }
            }
        } else {
            paramArrays = new Object[]{};
        }

        logSql(new Sql(sql, paramArrays));
        sqLiteDatabase.execSQL(sql, paramArrays);
        return 1;
    }

    /**
     * 执行SQL语句，返回执行行数
     *
     * @param sql
     * @return
     */
    public int execute(Sql sql) {
        return execute(sql.getSql(), sql.getParams());
    }

    public int[] execute(Collection<Sql> sqls) {
        int[]         result   = new int[sqls.size()];
        int           index    = 0;
        Sql           sql;
        Iterator<Sql> iterator = sqls.iterator();
        while (iterator.hasNext()) {
            sql = iterator.next();
            result[index++] = execute(sql.getSql(), sql.getParams());
        }
        return result;
    }

    protected IDialect getDialect() {
        if (dialect == null) {
            dialect = new SQLiteDialect();
        }
        return dialect;
    }

    protected Class<TModel> getMClass() {
        return this.clazz;
    }

    /**
     * 把指定类型的数组转为Object数组
     *
     * @param params
     * @param <T>
     * @return
     */
    protected <T> Object[] convert(T... params) {
        if (ValidateUtil.isBlank(params)) {
            return new Object[0];
        }
        Object[] result = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            result[i] = params[i];
        }
        return result;
    }

    /**
     * 把指定类型的集合转为Object数组
     *
     * @param params
     * @param <T>
     * @return
     */
    protected <T> Object[] convert(Iterable<T> params) {
        if (params == null) {
            return new Object[0];
        }
        List result   = new ArrayList<>();
        Iterator<T> iterator = params.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result.toArray();
    }

    @NonNull
    private <TView> List<TView> toEntities(Class<TView> clazz, Cursor cursor) {
        List<TView> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int rowCount = cursor.getCount();
            for (int i = 0; i < rowCount; i++) {
                if (cursor == null)
                    break;
                result.add(toEntity(clazz, cursor));
                cursor.moveToNext();//移动到指定记录
            }
        }
        /*new add*/
        cursor.close();
        /*new add*/
        return result;
    }

    private <TView> TView toEntity(Class<TView> clazz,
                                   Cursor cursor) {
        TView      entity     = null;                       //返回实体
        ColumnMeta columnMeta = null;                       //列 元数据
        String columnName = null;                       //列名
        String fieldName  = null;                       //属性名
        Method setter     = null;                       //Setter方法
        Class<?> paramType  = null;                       //Setter参数类型
        Object propVal    = null;                       //属性值

        try {
            int columnCount = cursor.getColumnCount();
            if (columnCount > 1) {
                if (columnCount == 2 && cursor.getColumnName(0).equals("rownumber")) {
                    int columnIndex = 1;
                    for (Iterator<PropertyHandler> propertyHandlerIterator = PropertyHandlerFactory.getHandlers().iterator(); propertyHandlerIterator
                            .hasNext(); ) {
                        PropertyHandler propertyHandler = propertyHandlerIterator.next();
                        if (propertyHandler.match(clazz) == false) continue;

                        propVal = propertyHandler.getColumnValue(cursor, columnIndex);
                        entity = (TView) propVal;
                        break;
                    }
                } else {
                    entity = clazz.newInstance();

                    for (Iterator<ColumnMeta> iterator = MetaData.columns(clazz, ColumnType.READ_ONLY).iterator(); iterator.hasNext(); ) {
                        columnMeta = iterator.next();

                        columnName = columnMeta.getColumnName();
                        fieldName = columnMeta.getFieldName();
                        //判断ResultSet中是否包含该列
                        if (cursor.getColumnIndex(columnName) == -1) continue;

                        //获取Setter方法
                        setter = ReflectUtil.getSetter(clazz, fieldName);
                        if (setter == null || setter.getParameterTypes().length != 1) continue;

                        //获取Setter参数值类型
                        paramType = setter.getParameterTypes()[0];

                        for (Iterator<PropertyHandler> propertyHandlerIterator = PropertyHandlerFactory.getHandlers().iterator(); propertyHandlerIterator
                                .hasNext(); ) {
                            PropertyHandler propertyHandler = propertyHandlerIterator.next();
                            if (propertyHandler.match(paramType) == false) continue;

                            propVal = propertyHandler.getColumnValue(cursor, columnName);
                            setter.invoke(entity, propVal);
                            break;
                        }

                        //初始化字段
                        columnMeta = null;                      //列 元数据
                        columnName = null;                      //列名
                        fieldName = null;                       //属性名
                        setter = null;                          //Setter方法
                        paramType = null;                       //Setter参数类型
                        propVal = null;                         //属性值
                    }
                }
            } else {
                int columnIndex = 0;
                for (Iterator<PropertyHandler> propertyHandlerIterator = PropertyHandlerFactory.getHandlers().iterator(); propertyHandlerIterator
                        .hasNext(); ) {
                    PropertyHandler propertyHandler = propertyHandlerIterator.next();
                    if (propertyHandler.match(clazz) == false) continue;

                    propVal = propertyHandler.getColumnValue(cursor, columnIndex);
                    entity = (TView) propVal;
                    break;
                }
            }
        } catch (Exception e) {
            StringBuilder errorMsg = new StringBuilder("转换ResultSet为实体对象时发生错误");
            if (ValidateUtil.isNotBlank(columnName)) errorMsg.append(", 数据库字段：").append(columnName);
            if (ValidateUtil.isNotBlank(fieldName)) errorMsg.append(", 属性名称：").append(fieldName);
            if (ValidateUtil.isNotBlank(paramType))
                errorMsg.append(", 属性类型：").append(paramType.getName());
            if (ValidateUtil.isNotBlank(propVal)) errorMsg.append(", 属性值：").append(propVal);
            errorMsg.append(", 错误信息：").append(e.getMessage());
//            Logger.e(errorMsg.toString(), e);
            throw new DatabaseException(e);
        }

        return entity;
    }


}
