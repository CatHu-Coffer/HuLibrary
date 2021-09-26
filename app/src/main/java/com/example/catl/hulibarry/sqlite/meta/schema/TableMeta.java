package com.example.catl.hulibarry.sqlite.meta.schema;

/**
 * 数据表元数据
 * Created by tanqimin on 2015/12/30.
 */
public class TableMeta {
    /**
     * 数据表名称
     */
    private String tableName;
    /**
     * java类名称
     */
    private String className;
    /**
     * 是否使用缓存？
     */
    private boolean cached;

    /**
     * 缓存过期时间，默认-1
     */
    private int expired;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean getCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }
}
