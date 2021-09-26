package com.example.catl.hulibarry.sqlite.persistence.handler.impls;


import android.content.ContentValues;
import android.database.Cursor;

import com.example.catl.hulibarry.sqlite.persistence.handler.PropertyHandler;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;


import java.sql.SQLException;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class ObjectPropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return true;
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
        return cursor.getString(columnIndex);
    }

    @Override
    public void setColumnValue(
            ContentValues contentValues,
            String columnName,
            Object columnValue)
            throws SQLException {
        String value = null;
        if (ValidateUtil.isNotBlank(columnValue))
            value = columnValue.toString();
        contentValues.put(columnName, value);
    }
}
