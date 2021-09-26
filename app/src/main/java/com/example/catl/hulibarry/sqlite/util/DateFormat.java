package com.example.catl.hulibarry.sqlite.util;

import java.text.SimpleDateFormat;

/**
 * Created by tanqimin on 2016/11/12.
 */

public interface DateFormat {
    String DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
    String DATE      = "yyyy-MM-dd";
    String TIME      = "HH:mm:ss";

    SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME);
    SimpleDateFormat DATE_FORMAT      = new SimpleDateFormat(DATE);
    SimpleDateFormat TIME_FORMAT      = new SimpleDateFormat(TIME);
}
