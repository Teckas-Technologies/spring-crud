package com.example.Demo.model.common;

import java.util.List;

public class PageResponse {

    private List<?> data;

    private int pageNumber = 1;

    private int pageSize = 10;

    private int totalPages = 0;

    public PageResponse() {
    }

    public PageResponse(List<?> data, int pageNumber, int pageSize, int totalPages) {
        this.data = data;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
