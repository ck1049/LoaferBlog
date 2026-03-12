package com.loafer.blog.service.impl;

import com.loafer.blog.config.FileSizeLimitCache;
import com.loafer.blog.model.dto.FileSizeLimitDTO;
import com.loafer.blog.service.ConfigurationService;
import com.loafer.blog.service.FileLimitService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 文件大小限制服务实现类
 * @author loafer
 * @since 2026-03-12 20:50:20
 **/
@AllArgsConstructor
@Service
public class FileLimitServiceImpl implements FileLimitService {

    private final ConfigurationService configurationService;
    private final FileSizeLimitCache fileSizeLimitCache;

    @Override
    public FileSizeLimitDTO getFileLimits() {
        FileSizeLimitDTO dto = new FileSizeLimitDTO();

        // 读取图片文件限制
        String imageMaxSizeStr = configurationService.getValueByModuleAndKey("file", "image_max_size", "10485760");
        dto.setImageMaxSize(Long.parseLong(imageMaxSizeStr));
        dto.setImageDescription("图片文件上传最大大小（字节）");

        // 读取视频文件限制
        String videoMaxSizeStr = configurationService.getValueByModuleAndKey("file", "video_max_size", "52428800");
        dto.setVideoMaxSize(Long.parseLong(videoMaxSizeStr));
        dto.setVideoDescription("视频文件上传最大大小（字节）");

        // 读取其他文件限制
        String otherMaxSizeStr = configurationService.getValueByModuleAndKey("file", "other_max_size", "20971520");
        dto.setOtherMaxSize(Long.parseLong(otherMaxSizeStr));
        dto.setOtherDescription("其他文件上传最大大小（字节）");
        return dto;
    }

    @Override
    public FileSizeLimitDTO updateFileLimits(FileSizeLimitDTO dto) {
        // 更新图片文件限制
        configurationService.updateByModuleAndKey("file", "image_max_size", dto.getImageMaxSize().toString(), "图片文件上传最大大小（字节）");

        // 更新视频文件限制
        configurationService.updateByModuleAndKey("file", "video_max_size", dto.getVideoMaxSize().toString(), "视频文件上传最大大小（字节）");

        // 更新其他文件限制
        configurationService.updateByModuleAndKey("file", "other_max_size", dto.getOtherMaxSize().toString(), "其他文件上传最大大小（字节）");

        // 清空缓存
        fileSizeLimitCache.clearCache();
        return getFileLimits();
    }
}
