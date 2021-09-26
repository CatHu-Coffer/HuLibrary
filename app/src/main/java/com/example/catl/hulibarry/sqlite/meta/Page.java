package com.example.catl.hulibarry.sqlite.meta;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页结果类
 * Created by tanqimin on 2015/11/4.
 */
public class Page<T> implements Serializable {
    private List<T> data;
    private int currentPage;
    private int recordsPerPage;
    private int totalPages;
    private int totalRecords;

    public Page(List<T> data, int currentPage, int recordsPerPage, int totalPages, int totalRecords) {
        this.data = data;
        this.currentPage = currentPage;
        this.recordsPerPage = recordsPerPage;
        this.totalPages = totalPages;
        this.totalRecords = totalRecords;
    }

    /**
     * 返回空白页数据
     *
     * @param currentPage
     * @param recordsPerPage
     * @param <T>
     * @return
     */
    public static <T> Page<T> blankPage(int currentPage, int recordsPerPage) {
        return new Page<>(Collections.EMPTY_LIST, currentPage, recordsPerPage, 0, 0);
    }

    /**
     * 获取数据集
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 设置数据集
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 获取当前页码
     *
     * @return
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 设置当前页码
     *
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 获取每页记录数
     *
     * @return
     */
    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    /**
     * 设置每页记录数
     *
     * @param recordsPerPage
     */
    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 设置总页数
     *
     * @param totalPages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * 设置总记录数
     *
     * @param totalRecords
     */
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * 获取是否拥有下一页
     *
     * @return
     */
    public boolean getHasNext() {
        return totalPages > currentPage;
    }

    @Override
    public String toString() {
        return "Page{" +
                "data=" + data +
                ", currentPage=" + currentPage +
                ", recordsPerPage=" + recordsPerPage +
                ", totalPages=" + totalPages +
                ", totalRecords=" + totalRecords +
                '}';
    }
}
