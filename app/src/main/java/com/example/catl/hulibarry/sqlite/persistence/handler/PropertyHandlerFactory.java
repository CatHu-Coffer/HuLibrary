package com.example.catl.hulibarry.sqlite.persistence.handler;




import com.example.catl.hulibarry.sqlite.persistence.handler.impls.BigDecimalPropertyHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.BooleanPropertyHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.BytePropertyHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.DatePropertyHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.DoubleColumnHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.FloatColumnHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.IntegerColumnHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.LongColumnHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.ObjectPropertyHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.ShortColumnHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.SqlDatePropertyHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.StringColumnHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.TimePropertyHandler;
import com.example.catl.hulibarry.sqlite.persistence.handler.impls.TimestampColumnHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanqimin on 2016/1/29.
 */
public class PropertyHandlerFactory {
    private final static List<PropertyHandler> handlers = new ArrayList<>();

    static {
        handlers.add(new StringColumnHandler());
        handlers.add(new DatePropertyHandler());
        handlers.add(new BooleanPropertyHandler());
        handlers.add(new BigDecimalPropertyHandler());
        handlers.add(new IntegerColumnHandler());
        handlers.add(new LongColumnHandler());
        handlers.add(new ShortColumnHandler());
        handlers.add(new DoubleColumnHandler());
        handlers.add(new FloatColumnHandler());
        handlers.add(new SqlDatePropertyHandler());
        handlers.add(new TimePropertyHandler());
        handlers.add(new TimestampColumnHandler());
        handlers.add(new BytePropertyHandler());
        handlers.add(new ObjectPropertyHandler());
    }

    public final static List<PropertyHandler> getHandlers() {
        return handlers;
    }
}
