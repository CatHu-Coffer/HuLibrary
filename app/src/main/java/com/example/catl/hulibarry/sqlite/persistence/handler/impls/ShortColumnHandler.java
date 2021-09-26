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
import com.example.catl.hulibarry.sqlite.util.ValidateUtil;

import java.sql.SQLException;

public class ShortColumnHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(Short.TYPE) || propType.equals(Short.class);
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
        return cursor.getShort(columnIndex);
    }

    @Override
    public void setColumnValue(
            ContentValues contentValues,
            String columnName,
            Object columnValue)
            throws SQLException {
        Short value = null;
        if (ValidateUtil.isNotBlank(columnValue))
            value = Short.parseShort(columnValue.toString());
        contentValues.put(columnName, value);
    }
}
