package com.example.catl.hulibarry.sqlite.persistence.handler.impls;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.catl.hulibarry.sqlite.persistence.handler.PropertyHandler;
import com.example.catl.hulibarry.sqlite.util.DateFormat;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;


import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class SqlDatePropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Date.class);
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
        SimpleDateFormat format = new SimpleDateFormat(DateFormat.DATE_TIME);
        java.util.Date   result = null;
        try {
            result = format.parse(cursor.getString(columnIndex));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(result.getTime());
    }

    @Override
    public void setColumnValue(
            ContentValues contentValues,
            String columnName,
            Object columnValue)
            throws SQLException {
        String value = null;
        if (ValidateUtil.isNotBlank(columnValue)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormat.DATE_TIME);
            value = simpleDateFormat.format(columnValue);
        }
        contentValues.put(columnName, value);
    }
}
