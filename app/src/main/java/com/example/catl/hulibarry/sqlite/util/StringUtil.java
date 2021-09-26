package com.example.catl.hulibarry.sqlite.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/3.
 */
public class StringUtil {
    public static CharSequence toString(Object object) {
        return object instanceof CharSequence ? (CharSequence) object : object.toString();
    }

    public static String join(CharSequence separator, Object[] objects) {
        StringBuilder StringBuilder = new StringBuilder();
        List<Object> objectList    = Arrays.asList(objects);
        Iterator<Object> iterator      = objectList.iterator();
        if (iterator.hasNext()) {
            StringBuilder.append(iterator.next());
            while (iterator.hasNext()) {
                StringBuilder.append(separator);
                StringBuilder.append(iterator.next());
            }
        }
        return StringBuilder.toString();
    }

    /**
     * 判断是否以指定空格开头
     *
     * @param str
     * @return
     */
    public static boolean startWithSpace(String str) {
        return str.startsWith(" ");
    }

    /**
     * 判断字符串是否以startStr开头，endStr结尾
     *
     * @param str      字符串
     * @param startStr 起始字符串
     * @param endStr   结束字符串
     * @return
     */
    public static boolean surroundWith(String str, String startStr, String endStr) {
        return str.startsWith(startStr) && str.endsWith(endStr);
    }

    /**
     * 移除字符串中包含的字符串
     *
     * @param str           字符串
     * @param removeStrings 移除的字符串
     * @return
     */
    public static String remove(String str, String... removeStrings) {
        String result = str;
        if (removeStrings != null && removeStrings.length > 0) {
            for (String removeStr : removeStrings) {
                result = result.replace(removeStr.toString(), "");
            }
        }
        return result;
    }

    /**
     * 首字母大写
     *
     * @param fieldName
     * @return
     */
    public static String capitalize(String fieldName) {
        if (ValidateUtil.isBlank(fieldName)) return "";
        StringBuilder sb = new StringBuilder(fieldName);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}
