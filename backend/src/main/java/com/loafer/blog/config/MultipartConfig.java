package com.loafer.blog.config;

import com.loafer.blog.model.dto.FileSizeLimitDTO;
import com.loafer.blog.service.FileLimitService;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@AllArgsConstructor
@Configuration
public class MultipartConfig {

    public final FileLimitService fileLimitService;

//    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置一个足够大的默认值，实际的文件大小限制会通过FileSizeLimitFilter进行动态检查
        long maxSize = 1073741824; // 1GB
        factory.setMaxFileSize(DataSize.ofBytes(maxSize));
        factory.setMaxRequestSize(DataSize.ofBytes(maxSize));
        return factory.createMultipartConfig();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        FileSizeLimitDTO fileLimits = fileLimitService.getFileLimits();
        return new StandardServletMultipartResolver();
    }
}
