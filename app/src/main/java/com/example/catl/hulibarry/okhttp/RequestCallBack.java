package com.example.catl.hulibarry.okhttp;

/**
 * Created by HKR on 2017/6/7.
 */

public interface RequestCallBack<T> {
    /**
     * 响应成功
     */
    void onReqSuccess(T result);

    /**
     * 响应失败
     */
    void onReqFailed(String errorMsg);
}
