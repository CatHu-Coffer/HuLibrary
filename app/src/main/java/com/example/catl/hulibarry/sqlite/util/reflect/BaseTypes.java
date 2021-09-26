package com.example.catl.hulibarry.sqlite.util.reflect;



import com.example.catl.hulibarry.sqlite.util.ValidateUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 基础类型
 * Created by tanqimin on 2015/12/7.
 */
public class BaseTypes extends ArrayList<String> {
    private static final BaseTypes BASE_TYPES = new BaseTypes();

    private BaseTypes(int initialCapacity) {
        super(initialCapacity);
    }

    private BaseTypes() {
    }

    private BaseTypes(Collection<? extends String> c) {
        super(c);
    }

    static {
        BASE_TYPES.add("String");
        BASE_TYPES.add("boolean");
        BASE_TYPES.add("Boolean");
        BASE_TYPES.add("int");
        BASE_TYPES.add("Integer");
        BASE_TYPES.add("long");
        BASE_TYPES.add("Long");
        BASE_TYPES.add("Date");
        BASE_TYPES.add("BigDecimal");
        BASE_TYPES.add("BigDecimal");
        BASE_TYPES.add("Float");
        BASE_TYPES.add("float");
        BASE_TYPES.add("Double");
        BASE_TYPES.add("double");
        BASE_TYPES.add("byte");
        BASE_TYPES.add("Byte");
        BASE_TYPES.add("byte[]");
        BASE_TYPES.add("Byte[]");
        BASE_TYPES.add("void");
        BASE_TYPES.add("Short");
        BASE_TYPES.add("short");
    }

    public static BaseTypes getInstance() {
        return BASE_TYPES;
    }

    /**
     * 判断传入类型是否基础类型
     *
     * @param simpleTypeName
     * @return
     */
    public static boolean isBaseType(String simpleTypeName) {
        for (String baseType : BASE_TYPES) {
            if (ValidateUtil.equalsIgnoreCase(simpleTypeName, baseType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBaseType(Class<?> clazz) {
        return isBaseType(clazz.getSimpleName());
    }
}
