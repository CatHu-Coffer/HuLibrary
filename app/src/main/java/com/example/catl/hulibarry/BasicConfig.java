package com.example.catl.hulibarry;

import android.content.Context;

/**
 * Created by HSL on 2020/12/23.
 */

public class BasicConfig {

    public static Context ApplicationContext;
    private static boolean DebugMode = true; //日志是否打印
    private static boolean LogToFile = false; //是否打印成文件保存

    public static void init(Context context) {
        ApplicationContext = context.getApplicationContext();
    }

    public static boolean isDebugMode() {
        return DebugMode;
    }

    public static void setDebugMode(boolean debugMode) {
        DebugMode = debugMode;
    }

    public static boolean isLogToFile() {
        return LogToFile;
    }

    public static void setLogToFile(boolean logToFile) {
        LogToFile = logToFile;
    }
}
