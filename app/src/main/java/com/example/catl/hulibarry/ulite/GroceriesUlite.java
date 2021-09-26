package com.example.catl.hulibarry.ulite;

/**
 * 杂货工具类,一些小帮助类
 * Created by 123 on 2018/2/26.
 */
public class GroceriesUlite {

    /**
     * 传入系统时间轴转换格式
     * @param millisecond
     * @return
     */
    public static String updataTime(int millisecond) {
        int seconde = millisecond / 1000;
        int hh = seconde / 3600;
        int mm = seconde % 3600 / 60;
        int ss = seconde % 60;
        String str = null;
        if (hh != 0) {
            str = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format("%02d:%02d", mm, ss);
        }
        return str;
    }

}
