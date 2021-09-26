package com.example.catl.hulibarry.sqlite.persistence;

import android.database.sqlite.SQLiteDatabase;

import com.example.catl.hulibarry.sqlite.meta.Page;
import com.example.catl.hulibarry.sqlite.meta.Sql;

import java.util.Collection;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/4.
 */
public abstract class ModelDao<TModel>
        extends AbstractDao<TModel>
        implements IModelDao<TModel> {

    public ModelDao(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }

    protected List<TModel> find(
            String sql,
            Object... params) {
        return super.find(super.getMClass(), sql, params);
    }

    protected List<TModel> find(Sql sql) {
        return super.find(super.getMClass(), sql);
    }

    protected List<TModel> find(
            Sql sql,
            boolean cached) {
        return super.find(super.getMClass(), sql, cached);
    }

    protected List<TModel> findTop(
            int top,
            String sql,
            Object... params) {
        return super.findTop(super.getMClass(), top, sql, params);
    }

    protected List<TModel> findTop(
            int top,
            Sql sql) {
        return super.findTop(super.getMClass(), top, sql);
    }

    protected List<TModel> findTop(
            int top,
            Sql sql,
            boolean cached) {
        return super.findTop(super.getMClass(), top, sql, cached);
    }

    protected Page<TModel> findByPage(
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            String sql,
            Object... params) {
        return super.findByPage(super.getMClass(), isPageable, currentPage, recordsPerPage, sql, params);
    }

    protected Page<TModel> findByPage(
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            Sql sql) {
        return super.findByPage(super.getMClass(), isPageable, currentPage, recordsPerPage, sql);
    }

    protected Page<TModel> findByPage(
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            Sql sql,
            boolean cached) {
        return super.findByPage(super.getMClass(), isPageable, currentPage, recordsPerPage, sql, cached);
    }

    @Override
    public TModel getById(Object primaryKey) {
        return super.getById(super.getMClass(), primaryKey);
    }

    @Override
    public TModel getById(
            Object primaryKey,
            boolean cached) {
        return super.getById(super.getMClass(), primaryKey, cached);
    }

    protected TModel get(
            String sql,
            Object... params) {
        return super.get(super.getMClass(), sql, params);
    }

    protected TModel get(Sql sql) {
        return super.get(super.getMClass(), sql);
    }

    protected TModel get(
            Sql sql,
            boolean cached) {
        return super.get(super.getMClass(), sql, cached);
    }

    @Override
    public int save(TModel model) {
        return super.save(super.getMClass(), model);
    }

    @Override
    public int save(Collection<TModel> models) {
        return super.save(super.getMClass(), models);
    }

    @Override
    public int update(TModel model) {
        return super.update(super.getMClass(), model);
    }

    @Override
    public int update(
            TModel model,
            String columns) {
        return super.update(super.getMClass(), model, columns);
    }

    @Override
    public int update(Collection<TModel> models) {
        return super.update(super.getMClass(), models);
    }

    @Override
    public int update(
            Collection<TModel> models,
            String columns) {
        return super.update(super.getMClass(), models, columns);
    }

    /**
     * 保存保存或更新
     *
     * @param model
     * @return
     */
    public int saveOrUpdate(TModel model) {
        return super.saveOrUpdate(super.getMClass(), model);
    }

    /**
     * 批量保存或更新
     *
     * @param models
     * @return
     */
    public int saveOrUpdate(Collection<TModel> models) {
        return super.saveOrUpdate(super.getMClass(), models);
    }

    @Override
    public int delete(TModel model) {
        return super.delete(super.getMClass(), model);
    }

    @Override
    public int delete(Collection<TModel> models) {
        return super.delete(super.getMClass(), models);
    }

    @Override
    public int deleteById(Object primaryKey) {
        return super.deleteById(super.getMClass(), primaryKey);
    }

    @Override
    public int deleteByIds(Object... primaryKeys) {
        return super.deleteByIds(super.getMClass(), primaryKeys);
    }

    @Override
    public int deleteByIds(Collection primaryKeys) {
        return super.deleteByIds(super.getMClass(), primaryKeys);
    }

    @Override
    public int deleteByField(
            String fieldName,
            Object... params) {
        return super.deleteByField(super.getMClass(), fieldName, params);
    }

    @Override
    public int deleteByField(
            String fieldName,
            Collection params) {
        return super.deleteByField(super.getMClass(), fieldName, params);
    }


}
