package com.loafer.blog.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;


@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 使用默认值设置文件大小限制
        // 注意：这里不再从配置表读取，因为在应用启动时配置服务可能还未初始化
        // 实际的文件大小限制会在FileLimitController中通过ConfigurationService进行管理
        long maxSize = 10485760; // 默认10MB
        factory.setMaxFileSize(DataSize.ofBytes(maxSize));
        factory.setMaxRequestSize(DataSize.ofBytes(maxSize));
        return factory.createMultipartConfig();
    }
}
