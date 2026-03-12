package com.loafer.blog.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileSizeLimitCache {

    private final Map<String, Long> fileSizeLimits = new ConcurrentHashMap<>();

    public void setFileSizeLimit(String type, long size) {
        fileSizeLimits.put(type, size);
    }

    public Long getFileSizeLimit(String type) {
        return fileSizeLimits.get(type);
    }

    public void clearCache() {
        fileSizeLimits.clear();
    }

    public boolean isEmpty() {
        return fileSizeLimits.isEmpty();
    }
}
