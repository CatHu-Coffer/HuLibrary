package com.example.catl.hulibarry.ulite;

import android.util.Log;

/**
 * Created by 123 on 2018/4/13.
 */

public class LogUtile {

    private static boolean showFlag = true;
    private static String TAG = "LogUtile_data";
    private static int logLenth = 2000;


    public static void d(String str) {
        if (showFlag) {
            if (str.length() > logLenth) {
               d_long(str, logLenth);
            } else
                Log.d(TAG, str);

        }
    }

    private static void d_long(String str, int cutLength) {
        String substring = str.substring(0, cutLength);
        String endStr = str.substring(cutLength, str.length());
        if (endStr.length() > cutLength) {
            Log.d(TAG, substring);
           d_long(endStr, cutLength);
        } else
            Log.d(TAG, endStr);
    }


    public static void Log_d(String str, Exception exception) {
        if (showFlag) {
            if (str.length() > logLenth) {
                d_long(str, logLenth);
            } else
                Log.d(TAG, str, exception);
        }
    }


    public static void e(String str) {
        if (showFlag) {
            if (str.length() > logLenth) {
                e_long(str, logLenth);
            } else
                Log.e(TAG, str);
        }
    }


    public static void e(String str, Exception exception) {
        if (showFlag) {
            if (str.length() > logLenth) {
                e_long(str, logLenth);
            } else
                Log.e(TAG, str, exception);
        }
    }
    private static void e_long(String str, int cutLength) {
        String substring = str.substring(0, cutLength);
        String endStr = str.substring(cutLength, str.length());
        if (endStr.length() > cutLength) {
            Log.e(TAG, substring);
            e_long(endStr, cutLength);
        } else
            Log.e(TAG, endStr);
    }

    public static void i(String str) {
        if (showFlag) {
            if (str.length() > logLenth) {
                i_long(str, logLenth);
            } else
                Log.i(TAG, str);
        }
    }

    public static void i(String str, Exception exception) {
        if (showFlag) {
            if (str.length() > logLenth) {
               i_long(str, logLenth);
            } else
                Log.i(TAG, str, exception);
        }
    }

    private static void i_long(String str, int cutLength) {
        String substring = str.substring(0, cutLength);
        String endStr = str.substring(cutLength, str.length());
        if (endStr.length() > cutLength) {
            Log.i(TAG, substring);
           i_long(endStr, cutLength);
        } else
            Log.i(TAG, endStr);
    }
}
