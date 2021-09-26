package com.example.catl.hulibarry.ulite;

import android.os.Handler;
import android.os.Message;

import com.example.catl.hulibarry.interface_ulite.OnReadFileListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/1/11.
 */

public class ReadFileUlite {

    private OnReadFileListener mOnReadFileListener;
    private static final int VIDEO_OK = 0;
    private List<String> datas = new ArrayList<>();
    private static ReadFileUlite mReadFileUlite;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VIDEO_OK:
                    if (mOnReadFileListener != null) {
                        mOnReadFileListener.readVideoFiles(datas);
                    }
                    break;

            }
        }
    };


    public static ReadFileUlite getIntance() {
        if (mReadFileUlite == null) {
            synchronized (ReadFileUlite.class) {
                mReadFileUlite = new ReadFileUlite();
                return mReadFileUlite;
            }
        } else return mReadFileUlite;
    }

    public void readVideoFiles(final File file, OnReadFileListener mOnReadFileListener) {
        this.mOnReadFileListener = mOnReadFileListener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                datas.clear();
                showFiles(file);
                mHandler.sendEmptyMessage(VIDEO_OK);
            }
        }).start();
    }


    private void showFiles(File file) {
        if (file == null) return;
        File[] files = file.listFiles();
        if (files == null) return;
        for (File file1 : files) {
            if (file1.isDirectory()) {
                showFiles(file1);
            } else {
                String name = file1.getPath();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".ts")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".mov")
                            || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".m3u8")
                            || name.equalsIgnoreCase(".3gpp")
                            || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".mkv")
                            || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".divx")
                            || name.equalsIgnoreCase(".f4v")
                            || name.equalsIgnoreCase(".rm")
                            || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".ram")
                            || name.equalsIgnoreCase(".mpg")
                            || name.equalsIgnoreCase(".v8")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".m2v")
                            || name.equalsIgnoreCase(".asx")
                            || name.equalsIgnoreCase(".ra")
                            || name.equalsIgnoreCase(".ndivx")
                            || name.equalsIgnoreCase(".xvid")
                            ) {
                        datas.add(file1.getPath());
                    }
                }
            }
        }
    }

}
