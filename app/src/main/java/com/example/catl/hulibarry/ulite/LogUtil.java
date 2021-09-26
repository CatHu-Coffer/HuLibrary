package com.example.catl.hulibarry.ulite;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.catl.hulibarry.BasicConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HSL on 2020/12/23.
 */

public class LogUtil {
    static {
        HandlerThread handlerThread = new HandlerThread("LogToFile");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        mLogFileHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                LogFileInfo info = (LogFileInfo) msg.obj;
                writeLogFile(BasicConfig.ApplicationContext, info.level, info.logFileName, info.tag, info.message,
                        info.throwable);
            }
        };
    }

    private static final Handler mLogFileHandler;
    //    private static final String LogFileName = FileUtils.makeNameInCurrentTime() + ".txt";
    private static final String LogFileName = getSDPath() + "/Leader/" + "OkHttpManager.txt";

    private static class LogFileInfo {
        public int level = Log.DEBUG;
        public String logFileName;
        public String tag;
        public String message;
        public Throwable throwable;
    }

    private static final class LogFileWriter extends FileWriter {
        private String mPath;
        private boolean mClosed = false;

        public LogFileWriter(File file, boolean append) throws IOException {
            super(file, append);
            mPath = file.getAbsolutePath();
        }

        public LogFileWriter(File file) throws IOException {
            super(file);
            mPath = file.getAbsolutePath();
        }

        public LogFileWriter(String filename, boolean append) throws IOException {
            super(filename, append);
            mPath = filename;
        }

        public LogFileWriter(String filename) throws IOException {
            super(filename);
            mPath = filename;
        }

        public String getFilePath() {
            return mPath;
        }

        @Override
        public void close() throws IOException {
            super.close();
            mClosed = true;
        }

        public boolean isClosed() {
            return mClosed;
        }
    }

    public static File getLogDirectory(Context context) {
        return FileUtils.getDirectory(context, "log");
    }

    public static synchronized void deleteAllLogFiles(Context context) {
        File directory = FileUtils.getExternalDirectory(context, "log");
        FileUtils.deleteFiles(directory);
        directory = FileUtils.getInternalDirectory(context, "log");
        FileUtils.deleteFiles(directory);
    }

    public final static String getLevelText(int level) {
        switch (level) {
            case Log.VERBOSE:
                return "VERBOSE";
            case Log.DEBUG:
                return "DEBUG";
            case Log.INFO:
                return "INFO";
            case Log.WARN:
                return "WARN";
            case Log.ERROR:
                return "ERROR";
            default:
                return "UNKNOW";
        }
    }

    public static void i(String tag, String msg) {
        if (BasicConfig.isDebugMode()) {
            Log.i(tag, msg == null ? "" : msg);
            logToFile(Log.INFO, tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (BasicConfig.isDebugMode()) {
            Log.i(tag, msg == null ? "" : msg, tr);
            logToFile(Log.INFO, tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (BasicConfig.isDebugMode()) {
            Log.d(tag, msg == null ? "" : msg);
            logToFile(Log.DEBUG, tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BasicConfig.isDebugMode()) {
            Log.d(tag, msg == null ? "" : msg, tr);
            logToFile(Log.DEBUG, tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (BasicConfig.isDebugMode()) {
            Log.e(tag, msg == null ? "" : msg);
            logToFile(Log.ERROR, tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (BasicConfig.isDebugMode()) {
            Log.e(tag, msg == null ? "" : msg, tr);
            logToFile(Log.ERROR, tag, msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        if (BasicConfig.isDebugMode()) {
            Log.v(tag, msg == null ? "" : msg);
            logToFile(Log.VERBOSE, tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (BasicConfig.isDebugMode()) {
            Log.v(tag, msg == null ? "" : msg, tr);
            logToFile(Log.VERBOSE, tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (BasicConfig.isDebugMode()) {
            Log.w(tag, msg == null ? "" : msg);
            logToFile(Log.WARN, tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (BasicConfig.isDebugMode()) {
            Log.w(tag, msg == null ? "" : msg, tr);
            logToFile(Log.WARN, tag, msg, tr);
        }
    }

    /**
     * 保存成文件
     *
     * @param logFileName 文件的位置
     */
    public static void logToFile(String logFileName, int level, String tag, String message, Throwable throwable) {
        if (BasicConfig.isLogToFile()) {
            LogFileInfo info = new LogFileInfo();
            info.logFileName = logFileName;
            info.level = level;
            info.tag = tag;
            info.message = message;
            info.throwable = throwable;
            Message msg = mLogFileHandler.obtainMessage(0, info);
            msg.sendToTarget();
        }
    }

    public static void logToFile(String logFileName, int level, String tag, String message) {
        logToFile(logFileName, level, tag, message, null);
    }

    public static void logToFile(String logFileName, String tag, String message) {
        logToFile(logFileName, Log.DEBUG, tag, message, null);
    }

    public static void logToFile(int level, String tag, String message, Throwable throwable) {
        logToFile(LogFileName, level, tag, message, throwable);
    }

    public static void logToFile(int level, String tag, String message) {
        logToFile(LogFileName, level, tag, message);
    }

    public static void logToFile(String tag, String message) {
        logToFile(LogFileName, tag, message);
    }

    private static Map<String, LogFileWriter> mWriterMap = Collections
            .synchronizedMap(new HashMap<String, LogFileWriter>());

    private static FileWriter ensureFileWriter(Context context, String logFileName) {
        LogFileWriter writer = mWriterMap.get(logFileName);
        if (writer == null || writer.isClosed()) {
            try {
                File file = FileUtils.getFile(context, "log", logFileName);
                LogUtil.d("ensureFileWriter", file.getAbsolutePath() + "; " + file.exists());
                writer = new LogFileWriter(file, true);
                mWriterMap.put(logFileName, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return writer;
    }

    private static void writeLogFile(FileWriter writer, String text) {
        if (writer != null) {
            try {
                StringBuffer sb = new StringBuffer();
                sb.append(text).append('\n');
                writer.write(sb.toString());
                writer.flush();
                // LogUtil.v("writeLogFile", text);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    private static void writeLogFile(FileWriter writer, int level, String tag, String msg, Throwable throwable,
                                     long millisTime) {
        StringBuffer sb = new StringBuffer();
        sb.append(FileUtils.makeNameInTime(millisTime)).append("     ");
        // sb.append(getLevelText(level)).append("    ");
        sb.append(tag).append("    ");
        sb.append(msg);
        if (throwable != null) {
            sb.append('\n');
            sb.append(throwable.toString());
        }
        writeLogFile(writer, sb.toString());
    }

    private static void writeLogFile(Context context, int level, String logFileName, String tag, String message,
                                     Throwable throwable) {
        FileWriter writer = ensureFileWriter(context, logFileName);
        writeLogFile(writer, level, tag, message, throwable, System.currentTimeMillis());
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
        String dir = sdDir.toString();
        return dir;

    }

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
}
