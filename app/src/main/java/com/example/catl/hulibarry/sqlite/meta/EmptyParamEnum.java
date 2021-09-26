package com.example.catl.hulibarry.sqlite.meta;

/**
 * Created by tanqimin on 2016/1/23.
 */
public enum EmptyParamEnum {
    /**
     * 如果参数或参数集合为Null或个数为0，则忽略条件，返回所有记录
     */
    FETCH_ALL,
    /**
     * 如果参数或参数集合为Null或个数为0，则不返回记录
     */
    FETCH_NONE
}
