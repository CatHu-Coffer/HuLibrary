/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.catl.hulibarry.sqlite.persistence.handler.impls;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.catl.hulibarry.sqlite.persistence.handler.PropertyHandler;
import com.example.catl.hulibarry.sqlite.util.DateFormat;
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimestampColumnHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Timestamp.class);
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
        return new Timestamp(result.getTime());
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
