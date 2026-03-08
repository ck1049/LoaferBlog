package com.loafer.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 静态资源映射配置
 * @author loafer
 * @since 2026-03-08 18:55:38
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 文件上传路径配置
    @Value("${file.upload.dir}")
    private String uploadPath;

    @Value("${file.access.prefix}")
    private String accessPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 核心：将URL前缀 /files/ 映射到本地存储路径
        registry.addResourceHandler(accessPath + "/**")
                .addResourceLocations("file:" + uploadPath + "/");
        // 注意：file: 前缀必须加，代表本地文件系统路径
    }
}