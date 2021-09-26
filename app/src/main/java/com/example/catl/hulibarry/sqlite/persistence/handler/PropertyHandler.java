package com.example.catl.hulibarry.sqlite.persistence.handler;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.SQLException;

/**
 * 类型处理接口
 * Created by tanqimin on 2016/1/29.
 */
public interface PropertyHandler {
    /**
     * 判断类型是否相等
     *
     * @param propType
     * @return
     */
    boolean match(
            Class<?> propType);

    /**
     * 从Cursor读出指定列的值
     *
     * @param cursor
     * @param columnName
     * @return
     * @throws SQLException
     */
    Object getColumnValue(
            Cursor cursor,
            String columnName)
            throws SQLException;

    /**
     * 从Cursor读出指定列的值
     *
     * @param cursor
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    Object getColumnValue(
            Cursor cursor,
            int columnIndex)
            throws SQLException;

    /**
     * 设置ContentValues指定列的值
     *
     * @param contentValues
     * @param columnName
     * @param columnValue
     * @throws SQLException
     */
    void setColumnValue(
            ContentValues contentValues,
            String columnName,
            Object columnValue)
            throws SQLException;
}
