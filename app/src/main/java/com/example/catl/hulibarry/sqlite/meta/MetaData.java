package com.example.catl.hulibarry.sqlite.meta;


import com.example.catl.hulibarry.sqlite.annotation.Column;
import com.example.catl.hulibarry.sqlite.annotation.Table;
import com.example.catl.hulibarry.sqlite.exception.DatabaseException;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnMeta;
import com.example.catl.hulibarry.sqlite.meta.schema.ColumnType;
import com.example.catl.hulibarry.sqlite.meta.schema.TableMeta;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;
import com.example.catl.hulibarry.sqlite.util.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanqimin on 2015/11/3.
 */
public class MetaData {
    private final static Map<String, TableMeta> TABLE_CACHE = new HashMap<>();
    private final static Map<String, List<ColumnMeta>> COLUMNS_CACHE = new HashMap<>();
    private final static Map<String, List<ColumnMeta>> READ_ONLY_COLUMNS_CACHE = new HashMap<>();

    /**
     * 根据 @Table 标记的实体类获取数据表名称
     *
     * @param clazz 实体类
     * @return
     */
    public static TableMeta table(Class<?> clazz) {
        String cacheKey = clazz.getName();
        if (TABLE_CACHE.containsKey(cacheKey)) {
            return TABLE_CACHE.get(cacheKey);
        }

        TableMeta tableMeta;
        Table tableAnn = clazz.getAnnotation(Table.class);
        if (tableAnn != null) {
            tableMeta = new TableMeta();
            tableMeta.setTableName(tableAnn.value().length() == 0 ? clazz.getSimpleName() : tableAnn.value());
            tableMeta.setClassName(clazz.getName());
            TABLE_CACHE.put(cacheKey, tableMeta);
            return table(clazz);
        }

        throw new DatabaseException("实体类必须标记 @Table 注释。");
    }

    /**
     * 根据 Field 获取 @Column 中的ColumnName
     *
     * @param field 实体字段
     * @return 如果没有标记@Column，则返回null
     */
    public static ColumnMeta getColumnByField(Field field) {
        return getColumnByFieldName(field.getDeclaringClass(), field.getName());
    }

    /**
     * 根据 Field 获取 @Column 中的ColumnName
     *
     * @param clazz     实体类
     * @param fieldName 实体属性名
     * @return 如果没有标记@Column，则返回null
     */
    public static ColumnMeta getColumnByFieldName(
            Class<?> clazz,
            String fieldName) {
        ColumnMeta columnMeta = null;
        List<ColumnMeta> columns = columns(clazz, ColumnType.READ_ONLY);
        for (ColumnMeta column : columns) {
            if (ValidateUtil.equalsIgnoreCase(fieldName, column.getFieldName())) {
                columnMeta = column;
                break;
            }
        }
        return columnMeta;
    }

    /**
     * 获取所有标记 @Column 字段的集合
     *
     * @param clazz      类
     * @param columnType 是否包含只读字段
     * @return
     */
    public static List<ColumnMeta> columns(
            Class clazz,
            ColumnType columnType) {
        List<ColumnMeta> columnMetas = new ArrayList<>();

        String cacheKey = clazz.getName();
        if (COLUMNS_CACHE.containsKey(cacheKey)) {
            columnMetas.addAll(COLUMNS_CACHE.get(cacheKey));
            if (columnType == ColumnType.READ_ONLY && READ_ONLY_COLUMNS_CACHE.containsKey(cacheKey)) {
                columnMetas.addAll(READ_ONLY_COLUMNS_CACHE.get(cacheKey));
            }
            return columnMetas;
        }

        List<ColumnMeta> readOnlyColumnMetas = new ArrayList<>();
        List<Field> fields = ReflectUtil.getFields(clazz);
        Column columnAnn;
        ColumnMeta columnMeta;
        for (Field field : fields) {
            columnAnn = field.getAnnotation(Column.class);
            if (columnAnn == null) {
                continue;
            }

            columnMeta = new ColumnMeta();
            columnMeta.setColumnName(columnAnn.value().length() == 0 ? field.getName() : columnAnn.value());
            columnMeta.setFieldName(field.getName());
            columnMeta.setReadOnly(columnAnn.readOnly());
//            if (field.getName().equals("id"))
//                columnMeta.setIsPrimaryKey(true);
//            else
            columnMeta.setIsPrimaryKey(columnAnn.isPrimaryKey());
            columnMeta.setFieldType(field.getType());

            if (columnAnn.readOnly()) {
                readOnlyColumnMetas.add(columnMeta);
            } else {
                columnMetas.add(columnMeta);
            }
        }
        COLUMNS_CACHE.put(cacheKey, columnMetas);
        READ_ONLY_COLUMNS_CACHE.put(cacheKey, readOnlyColumnMetas);

        return columns(clazz, columnType);
    }

    /**
     * 根据 @Table 标记的实体类获取主键（暂时不支持联合主键）
     *
     * @param clazz 实体类
     * @return
     */
    public static ColumnMeta getPrimaryKey(Class clazz) {
        ColumnMeta columnMeta = null;
        List<ColumnMeta> columns = columns(clazz, ColumnType.WRITABLE);
        for (ColumnMeta column : columns) {
            if (column.getIsPrimaryKey()) {
                columnMeta = column;
                break;
            }
        }
        return columnMeta;
    }

    /**
     * 根据数据库列名获取ColumnMeta
     *
     * @param clazz      实体类
     * @param columnName 数据库列名
     * @return
     */
    public static ColumnMeta getColumnByColumnName(
            Class clazz,
            String columnName) {
        ColumnMeta columnMeta = null;
        List<ColumnMeta> columns = columns(clazz, ColumnType.READ_ONLY);
        for (ColumnMeta column : columns) {
            if (ValidateUtil.equalsIgnoreCase(columnName, column.getColumnName())) {
                columnMeta = column;
                break;
            }
        }
        return columnMeta;
    }
}
