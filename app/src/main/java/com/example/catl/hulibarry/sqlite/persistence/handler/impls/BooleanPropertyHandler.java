package com.example.catl.hulibarry.sqlite.persistence.handler.impls;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.example.catl.hulibarry.sqlite.persistence.handler.PropertyHandler;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class BooleanPropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Boolean.TYPE) || propType.equals(Boolean.class);
    }

    @Override
    public Object getColumnValue(
            Cursor cursor,
            String columnName)
            throws SQLException {
        return getColumnValue(cursor, cursor.getColumnIndex(columnName));
    }

    @Override
    public Object getColumnValue(Cursor cursor, int columnIndex) throws SQLException {
        return cursor.getInt(columnIndex) == 0;
    }

    @Override
    public void setColumnValue(
            ContentValues contentValues,
            String columnName,
            Object columnValue)
            throws SQLException {
        Boolean value = null;
        if (ValidateUtil.isNotBlank(columnValue))
            value = Boolean.parseBoolean(columnValue.toString());
        contentValues.put(columnName, value);
    }
}
