package com.loafer.blog.config;

import com.loafer.blog.common.enums.FileType;
import com.loafer.blog.service.ConfigurationService;
import com.loafer.blog.utils.FileUploadUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.MultipartResolver;

import java.io.IOException;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Component
public class FileSizeLimitFilter implements Filter {

    private final ConfigurationService configurationService;
    private final FileSizeLimitCache fileSizeLimitCache;
    private final MultipartResolver multipartResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. 判断是否有文件上传
        boolean hasFile = FileUploadUtils.hasFileUpload(httpRequest, multipartResolver);
        if (hasFile) {
            // 2. 转换为 MultipartRequest，获取文件并判断类型
            MultipartRequest multipartRequest = multipartResolver.resolveMultipart(httpRequest);
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

            for (Map.Entry<String, MultipartFile> multipartFileEntry : fileMap.entrySet()) {
                // 3. 获取文件类型
                // String headerType = FileUploadUtils.getFileTypeFromHeader(multipartFileEntry.getValue());
                String realMediaType = FileUploadUtils.getRealFileType(multipartFileEntry.getValue());
                FileType fileType = FileType.getByMediaType(realMediaType);
                // 获取文件大小
                long fileSize = multipartFileEntry.getValue().getSize();
                if (fileSizeLimitCache.isEmpty()) {
                    synchronized (fileSizeLimitCache) {
                        if (fileSizeLimitCache.isEmpty()) {
                            loadFileSizeLimits();
                        }
                    }
                }
                long maxSize = fileSizeLimitCache.getFileSizeLimit(fileType.getType());
                log.info("上传文件类型：{}，文件Media类型: {}，文件大小: {}，该类型文件大小限制:{}", fileType, realMediaType, fileSize, maxSize);

                // 检查文件大小是否超过限制
                if (fileSize > maxSize) {
                    httpResponse.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "File size exceeds the limit");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
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

}
