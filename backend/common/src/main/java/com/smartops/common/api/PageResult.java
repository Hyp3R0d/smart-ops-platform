package com.smartops.common.api;

import java.util.List;

public class PageResult<T> {
    private Long total;
    private Long page;
    private Long pageSize;
    private List<T> records;

    public PageResult() {
    }

    public PageResult(Long total, Long page, Long pageSize, List<T> records) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.records = records;
    }

    public static <T> PageResult<T> of(Long total, Long page, Long pageSize, List<T> records) {
        return new PageResult<>(total, page, pageSize, records);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
