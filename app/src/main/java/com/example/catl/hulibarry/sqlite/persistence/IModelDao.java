package com.example.catl.hulibarry.sqlite.persistence;

import java.util.Collection;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/4.
 */
public interface IModelDao<TModel> {
    /**
     * 根据主键获取实体
     *
     * @param primaryKey
     * @return
     */
    TModel getById(Object primaryKey);

    /**
     * 根据主键获取实体
     *
     * @param primaryKey
     * @param cached
     * @return
     */
    TModel getById(
            Object primaryKey,
            boolean cached);

    /**
     * 根据主键获取实体
     *
     * @param primaryKeys
     * @return
     */
    List<TModel> findByIds(Object... primaryKeys);

    /**
     * 根据主键获取实体
     *
     * @param primaryKeys
     * @return
     */
    List<TModel> findByIds(Collection primaryKeys);

    /**
     * 根据主键获取实体
     *
     * @param primaryKeys
     * @param cached
     * @return
     */
    List<TModel> findByIds(
            Collection primaryKeys,
            boolean cached);

    /**
     * 保存
     *
     * @param model
     * @return
     */
    int save(TModel model);

    /**
     * 批量保存
     *
     * @param models
     * @return
     */
    int save(Collection<TModel> models);

    /**
     * 更新
     *
     * @param model
     * @return
     */
    int update(TModel model);

    /**
     * 更新
     *
     * @param model
     * @param columns
     * @return
     */
    int update(
            TModel model,
            String columns);

    /**
     * 批量更新
     *
     * @param models
     * @return
     */
    int update(Collection<TModel> models);

    /**
     * 批量更新
     *
     * @param models
     * @param columns
     * @return
     */
    int update(
            Collection<TModel> models,
            String columns);

    /**
     * 保存保存或更新
     *
     * @param model
     * @return
     */
    int saveOrUpdate(TModel model);

    /**
     * 批量保存或更新
     *
     * @param models
     * @return
     */
    int saveOrUpdate(Collection<TModel> models);

    /**
     * 删除
     *
     * @param model
     * @return
     */
    int delete(TModel model);

    /**
     * 删除
     *
     * @param models
     * @return
     */
    int delete(Collection<TModel> models);

    /**
     * 根据ID删除
     *
     * @param primaryKey
     * @return
     */
    int deleteById(Object primaryKey);

    /**
     * 根据ID集合删除
     *
     * @param primaryKeys
     * @return
     */
    int deleteByIds(Object... primaryKeys);

    /**
     * 根据ID集合删除
     *
     * @param primaryKeys
     * @return
     */
    int deleteByIds(Collection primaryKeys);

    /**
     * 根据指定字段删除对象
     *
     * @param fieldName
     * @param params
     * @return
     */
    int deleteByField(
            String fieldName,
            Object... params);

    /**
     * 根据指定字段删除对象
     *
     * @param fieldName
     * @param params
     * @return
     */
    int deleteByField(
            String fieldName,
            Collection params);


}
