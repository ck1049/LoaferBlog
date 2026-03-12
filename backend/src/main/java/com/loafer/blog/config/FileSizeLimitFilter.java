package com.loafer.blog.config;

import com.loafer.blog.service.ConfigurationService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FileSizeLimitFilter implements Filter {

    private final ConfigurationService configurationService;
    private final FileSizeLimitCache fileSizeLimitCache;

    public FileSizeLimitFilter(ConfigurationService configurationService, FileSizeLimitCache fileSizeLimitCache) {
        this.configurationService = configurationService;
        this.fileSizeLimitCache = fileSizeLimitCache;
        // 初始化缓存
        loadFileSizeLimits();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 检查是否是文件上传请求
        if (isMultipartRequest(httpRequest)) {
            // 获取请求的Content-Length
            String contentLengthStr = httpRequest.getHeader("Content-Length");
            if (contentLengthStr != null) {
                try {
                    long contentLength = Long.parseLong(contentLengthStr);
                    
                    // 根据文件类型获取对应的大小限制
                    long maxSize = getMaxFileSizeByType(httpRequest);
                    
                    // 检查文件大小是否超过限制
                    if (contentLength > maxSize) {
                        httpResponse.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "File size exceeds the limit");
                        return;
                    }
                } catch (NumberFormatException e) {
                    // 忽略无效的Content-Length
                }
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isMultipartRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.startsWith("multipart/");
    }

    private void loadFileSizeLimits() {
        // 从数据库加载文件大小限制并缓存
        String imageMaxSizeStr = configurationService.getValueByModuleAndKey("file", "image_max_size", "10485760");
        String videoMaxSizeStr = configurationService.getValueByModuleAndKey("file", "video_max_size", "52428800");
        String otherMaxSizeStr = configurationService.getValueByModuleAndKey("file", "other_max_size", "20971520");
        
        fileSizeLimitCache.setFileSizeLimit("image", Long.parseLong(imageMaxSizeStr));
        fileSizeLimitCache.setFileSizeLimit("video", Long.parseLong(videoMaxSizeStr));
        fileSizeLimitCache.setFileSizeLimit("other", Long.parseLong(otherMaxSizeStr));
    }

    private long getMaxFileSizeByType(HttpServletRequest request) {
        // 从缓存获取文件大小限制
        if (fileSizeLimitCache.isEmpty()) {
            loadFileSizeLimits();
        }
        
        // 根据文件类型判断
        String contentType = request.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return fileSizeLimitCache.getFileSizeLimit("image");
            } else if (contentType.startsWith("video/")) {
                return fileSizeLimitCache.getFileSizeLimit("video");
            }
        }
        
        // 默认使用其他文件限制
        return fileSizeLimitCache.getFileSizeLimit("other");
    }
}
