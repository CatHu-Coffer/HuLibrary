package com.example.catl.hulibarry.sqlite.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by tanqimin on 2015/5/12.
 */
public class ValidateUtil {
    private final static Pattern sqlPattern = Pattern.compile("(?:')|(?:--)|(/\\\\*(?:.|[\\\\n\\\\r])*?\\\\*/)");


    /**
     * 匹配正则表达式
     *
     * @param regex regex
     * @param value value
     * @return boolean
     */
    public static boolean match(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(value).find();
    }

    /**
     * 区分大小写
     *
     * @param regex regex
     * @param flags flags
     * @param value value
     * @return boolean
     */
    public static boolean match(String regex, int flags, String value) {
        Pattern pattern = Pattern.compile(regex, flags);
        return pattern.matcher(value).find();
    }

    /**
     * 邮箱验证工具
     *
     * @param value value
     * @return boolean
     */
    public static boolean isEmail(String value) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 手机号码验证
     *
     * @param value value
     * @return boolean
     */
    public static boolean isMobile(String value) {
        String check = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 座机验证
     *
     * @param value value
     * @return boolean
     */
    public static boolean isTel(String value) {
        String check = "^\\d{3,4}-?\\d{7,9}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 电话号码 包括移动电话和座机
     *
     * @param value value
     * @return boolean
     */
    public static boolean isPhone(String value) {
        String telcheck = "^\\d{3,4}-?\\d{7,9}$";
        String mobilecheck = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$";
        return match(telcheck, Pattern.CASE_INSENSITIVE, value) || match(mobilecheck, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value 输入内容限制为英文字母 、数字和下划线
     * @return boolean
     */
    public static boolean isGeneral(String value) {
        String check = "^\\w+$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value 输入内容限制为英文字母 、数字和下划线
     * @param min   最小长度
     * @param max   最大长度
     * @return boolean
     */
    public static boolean isGeneral(String value, int min, int max) {
        String check = "^\\w{" + min + "," + max + "}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 判断是否是生日
     *
     * @param value value
     * @return boolean
     */
    public static boolean isBirthDay(String value) {
        String check = "(\\d{4})(/|-|\\.)(\\d{1,2})(/|-|\\.)(\\d{1,2})$";

        if (match(check, Pattern.CASE_INSENSITIVE, value)) {
            int year = Integer.parseInt(value.substring(0, 4));
            int month = Integer.parseInt(value.substring(5, 7));
            int day = Integer.parseInt(value.substring(8, 10));

            if (month < 1 || month > 12) return false;

            if (day < 1 || day > 31) return false;

            if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) return false;

            if (month == 2) {
                boolean isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                if (day > 29 || (day == 29 && !isleap)) return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 身份证
     *
     * @param value value
     * @return boolean
     */
    public static boolean isIdentityCard(String value) {
        String check = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 邮政编码
     *
     * @param value value
     * @return boolean
     */
    public static boolean isZipCode(String value) {
        String check = "^[0-9]{6}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 货币
     *
     * @param value value
     * @return boolean
     */
    public static boolean isMoney(String value) {
        String check = "^(\\d+(?:\\.\\d{1,2})?)$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value 输入数字
     * @return boolean
     */
    public static boolean isNumber(String value) {
        String check = "^(\\+|\\-)?\\d+$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value 输入数字
     * @param min   最小长度
     * @param max   最大长度
     * @return boolean
     */
    public static boolean isNumber(String value, int min, int max) {
        String check = "^(\\+|\\-)?\\d{" + min + "," + max + "}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value 正整数
     * @return boolean
     */
    public static boolean isPositiveNumber(String value) {
        String check = "^\\d+$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value 正整数
     * @param min   最小长度
     * @param max   最大长度
     * @return boolean
     */
    public static boolean isPositiveNumber(String value, int min, int max) {
        String check = "^\\d{" + min + "," + max + "}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 中文
     *
     * @param value value
     * @return boolean
     */
    public static boolean isChinese(String value) {
        String check = "^[\\u2E80-\\u9FFF]+$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    public static boolean isChinese(String value, int min, int max) {
        String check = "^[\\u2E80-\\u9FFF]{" + min + "," + max + "}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value 中文字、英文字母、数字和下划线
     * @return boolean
     */
    public static boolean isString(String value) {
        String check = "^[\\u0391-\\uFFE5\\w]+$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value 中文字、英文字母、数字和下划线
     * @param min   最小长度
     * @param max   最大长度
     * @return boolean
     */
    public static boolean isString(String value, int min, int max) {
        String check = "^[\\u0391-\\uFFE5\\w]{" + min + "," + max + "}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * @param value UUID
     * @return boolean
     */
    public static boolean isUUID(String value) {
        String check = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 匹配是否是链接
     *
     * @param value value
     * @return boolean
     */
    public static boolean isUrl(String value) {
        String check = "^((https?|ftp):\\/\\/)?(((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:)*@)?(((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]))|((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?)(:\\d*)?)(\\/((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)+(\\/(([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)*)*)?)?(\\?((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)|[\\uE000-\\uF8FF]|\\/|\\?)*)?(\\#((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|@)|\\/|\\?)*)?$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    /**
     * 判断时间
     *
     * @param value value
     * @return boolean
     */
    public static boolean isDateTime(String value) {
        String check = "^(\\d{4})(/|-|\\.|年)(\\d{1,2})(/|-|\\.|月)(\\d{1,2})(日)?(\\s+\\d{1,2}(:|时)\\d{1,2}(:|分)?(\\d{1,2}(秒)?)?)?$";// check = "^(\\d{4})(/|-|\\.)(\\d{1,2})(/|-|\\.)(\\d{1,2})$";
        return match(check, Pattern.CASE_INSENSITIVE, value);
    }

    public static boolean isLength(String value, int min, int max) {
        int length = isBlank(value) ? 0 : value.length();
        return length >= min && length <= max;
    }

    public static boolean compareDate(String date1, String date2, String df) {
        SimpleDateFormat sdf = new SimpleDateFormat(df);
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            return d1.compareTo(d2) > 0;
        } catch (ParseException e) {
            return false;
        }
    }


    public static boolean compareDate(String date1, String date2) {
        return compareDate(date1, date2, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 检查传入语句是否包含SQL特殊符号，如：单引号(')，注释等
     *
     * @param sqlClause
     * @return
     */
    public static boolean isValidSql(String sqlClause) {
        return sqlPattern.matcher(sqlClause).find() == false;
    }

    /**
     * 检查传入对象是否为空值
     * 如果对象为Null，返回true
     * 如果对象是集合，且成员个数为0个，则返回true
     * 如果对象是数组，且成员个数为0个，则返回true
     * 如果对象是字符串，且为空字符串，则返回true
     *
     * @param value 值
     * @return
     */
    public static boolean isBlank(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof Iterable) {
            return ((Iterable) value).iterator().hasNext() == false;
        } else if (value.getClass().isArray()) {
            //数组
            Object[] objects = (Object[]) value;
            return objects.length == 0;
        } else {
            return "".equals(value.toString().trim());
        }
    }

    /**
     * 检查传入对象是否为非空
     *
     * @param value
     * @return
     */
    public static boolean isNotBlank(Object value) {
        return isBlank(value) == false;
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 != null && str2 == null) return false;
        if (str1 == null && str2 != null) return false;

        return str1.toUpperCase().equals(str2.toUpperCase());
    }


    public static boolean equals(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 != null && str2 == null) return str1.equals(str2);
        return str2.equals(str1);
    }
}
