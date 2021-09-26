package com.example.catl.hulibarry.sqlite.dialect;


import com.example.catl.hulibarry.sqlite.meta.Sql;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnMeta;

import java.util.List;

/**
 * Created by tanqimin on 2015/11/3.
 */
public interface IDialect {

    /**
     * 获取数据库方言名称
     *
     * @return
     */
    String getDialectName();

    /**
     * 获取插入语句
     *
     * @param <TModel>
     * @param clazz
     * @param entity
     * @return
     */
    <TModel> Sql insert(Class<TModel> clazz, TModel entity);

    /**
     * 获取插入语句
     *
     * @param <TModel>
     * @param clazz
     * @param columns
     * @return
     */
    <TModel> String insert(Class<TModel> clazz, List<ColumnMeta> columns);

    /**
     * 获取插入或更新语句
     *
     * @param <TModel>
     * @param clazz
     * @param entity
     * @return
     */
    <TModel> Sql insertOrUpdate(Class<TModel> clazz, TModel entity);

    /**
     * 获取插入或更新语句
     *
     * @param <TModel>
     * @param clazz
     * @param columns
     * @return
     */
    <TModel> String insertOrUpdate(Class<TModel> clazz, List<ColumnMeta> columns);

    /**
     * 获取根据主键查找语句
     *
     * @param clazz
     * @param id
     * @param <TModel>
     * @return
     */
    <TModel> Sql selectById(Class<TModel> clazz, Object id);

    /**
     * 获取所有记录语句
     *
     * @param clazz
     * @param <TModel>
     * @return
     */
    <TModel> Sql select(Class<TModel> clazz);

    /**
     * 根据 ID 集合获取实体语句
     *
     * @param clazz
     * @param primaryKeys
     * @param <TModel>
     * @return
     */
    <TModel> Sql selectByIds(Class<TModel> clazz, Object[] primaryKeys);

    /**
     * 获取返回行数语句
     *
     * @param sql
     * @param params
     * @return
     */
    Sql count(String sql, Object[] params);

    /**
     * 获取返回行数语句
     *
     * @param clazz
     * @param where
     * @param params
     * @param <TModel>
     * @return
     */
    <TModel> Sql countBy(Class<TModel> clazz, String where, Object[] params);

    /**
     * 返回分页查询语句
     *
     * @param currentPage
     * @param recordsPerPage
     * @param sql
     * @param params
     * @return
     */
    Sql selectTop(int currentPage, int recordsPerPage, String sql, Object[] params);

    /**
     * 获取更新语句
     *
     * @param clazz
     * @param model
     * @param <TModel>
     * @return
     */
    <TModel> Sql update(Class<TModel> clazz, TModel model);

    /**
     * 获取删除实体语句
     *
     * @param clazz
     * @param model
     * @param <TModel>
     * @return
     */
    <TModel> Sql delete(Class<TModel> clazz, TModel model);

    /**
     * 获取根据主键删除实体语句
     *
     * @param clazz
     * @param primaryKey
     * @param <TModel>
     * @return
     */
    <TModel> Sql deleteById(Class<TModel> clazz, Object primaryKey);

    /**
     * 根据主键集合删除主键
     *
     * @param clazz
     * @param primaryKeys
     * @param <TModel>
     * @return
     */
    <TModel> Sql deleteByIds(Class<TModel> clazz, Object[] primaryKeys);
}
