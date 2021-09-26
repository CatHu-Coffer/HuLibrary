package com.example.catl.hulibarry.sqlite.persistence.handler.impls;

import android.content.ContentValues;
import android.database.Cursor;


import com.example.catl.hulibarry.sqlite.persistence.handler.PropertyHandler;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class BytePropertyHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Byte.TYPE) || propType.equals(Byte.class);
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
        return cursor.getBlob(columnIndex);
    }

    @Override
    public void setColumnValue(
            ContentValues contentValues,
            String columnName,
            Object columnValue)
            throws SQLException {
        byte[] value = null;
        if (ValidateUtil.isNotBlank(columnValue)) {
            try {
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                     ObjectOutput out = new ObjectOutputStream(bos)) {
                    out.writeObject(columnValue);
                    value = bos.toByteArray();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        contentValues.put(columnName, value);
    }
}
