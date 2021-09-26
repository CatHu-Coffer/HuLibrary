package com.example.catl.hulibarry.ulite;

/**
 * 防止按钮多按工具类
 */

public class SecondClickUtils {
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 100) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
