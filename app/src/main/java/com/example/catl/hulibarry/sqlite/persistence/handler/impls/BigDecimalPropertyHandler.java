package com.example.catl.hulibarry.sqlite.persistence.handler.impls;

import android.content.ContentValues;
import android.database.Cursor;


import com.example.catl.hulibarry.sqlite.persistence.handler.PropertyHandler;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class BigDecimalPropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(BigDecimal.class);
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
        return new BigDecimal(cursor.getDouble(columnIndex));
    }

    @Override
    public void setColumnValue(
            ContentValues contentValues,
            String columnName,
            Object columnValue)
            throws SQLException {
        Double value = null;
        if (ValidateUtil.isNotBlank(columnValue))
            value = Double.parseDouble(columnValue.toString());
        contentValues.put(columnName, value);
    }
}
