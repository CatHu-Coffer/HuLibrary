package com.example.catl.hulibarry.ulite;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.IOException;

/**
 * 录音操作类
 * Created by 123 on 2018/4/18.
 */

public class AudioRecoderUtils {

    /**
     * 录音文件路径
     */
    private String filePath;

    /**
     * 录音文件夹路径
     */
    private String FolderPath;

    /**
     * 系统的录音工具
     */
    private MediaRecorder mediaRecorder;

    /**
     * 最大录取声音长度
     */
    private static final int MAX_TIME = 1000 * 60;

    /**
     * 最小录取声音长度
     */
    private static final int MIN_TIME = 1000 * 2;

    private long startTime; //开始时间
    private long endTime;   //结束时间

    /**
     * 回调监听录音操作
     */
    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param filePath 保存路径
         */
        void onStop(String filePath);
    }

    //监听事件
    private OnAudioStatusUpdateListener onAudioStatusUpdateListener;


    public AudioRecoderUtils(String filePath) {
        File path = new File(filePath);
        if (!path.exists())
            path.mkdirs();
        this.FolderPath = filePath;
    }

    /**
     * 文件存储默认sdcard/record
     */
    public AudioRecoderUtils() {
        //默认保存路径为/sdcard/record/下
        this(Environment.getExternalStorageDirectory() + "/record/");
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        //实例化media对象
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        }
        try {
            //设置声音来源，麦克风
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
      /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            filePath = FolderPath + System.currentTimeMillis() + ".amr";
            /* ③准备 */
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.setMaxDuration(MAX_TIME);
            mediaRecorder.prepare();
                    /* ④开始 */
            mediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
//            LogUtile.Log_d("startTime" + startTime);
        } catch (IOException e) {
//            LogUtile.Log_d(e.toString());
        }
    }


    /**
     * 停止录音
     */
    public long stopRecord() {
        if (mediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();

        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            onAudioStatusUpdateListener.onStop(filePath);
            filePath = "";

        } catch (RuntimeException e) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            File file = new File(filePath);
            if (file.exists())
                file.delete();

            filePath = "";

        }
        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {

        try {

            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

        } catch (RuntimeException e) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        File file = new File(filePath);
        if (file.exists())
            file.delete();

        filePath = "";

    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.onAudioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {

        if (mediaRecorder != null) {
            double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != onAudioStatusUpdateListener) {
                    onAudioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

}
