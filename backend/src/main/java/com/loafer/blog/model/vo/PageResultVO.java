package com.loafer.blog.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResultVO<T> {
    private List<T> records;
    private long total;
    private long size;
    private long current;
    private long pages;

    public PageResultVO() {
    }

    public PageResultVO(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = (total + size - 1) / size;
    }
}
