package com.example.catl.hulibarry.sqlite.meta.schema;

/**
 * 列 元数据
 * Created by tanqimin on 2015/12/30.
 */
public class ColumnMeta {
    /**
     * 数据库列名称
     */
    private String columnName;
    /**
     * java类字段名称
     */
    private String fieldName;
    /**
     * 是否只读字段？
     */
    private boolean readOnly;
    /**
     * 是否主键？
     */
    private boolean isPrimaryKey;

    /**
     * java类字段类型
     */
    private Class<?> fieldType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }
}
