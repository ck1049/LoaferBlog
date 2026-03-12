package com.loafer.blog.controller;

import com.loafer.blog.config.FileSizeLimitCache;
import com.loafer.blog.model.dto.FileSizeLimitDTO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.service.ConfigurationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/admin/file-limits")
public class FileLimitController {

    @Resource
    private ConfigurationService configurationService;
    
    @Resource
    private FileSizeLimitCache fileSizeLimitCache;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<FileSizeLimitDTO> getFileLimits() {
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
        
        return ResponseVO.success(dto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<FileSizeLimitDTO> updateFileLimits(@RequestBody FileSizeLimitDTO dto) {
        // 更新图片文件限制
        configurationService.updateByModuleAndKey("file", "image_max_size", dto.getImageMaxSize().toString(), dto.getImageDescription());
        
        // 更新视频文件限制
        configurationService.updateByModuleAndKey("file", "video_max_size", dto.getVideoMaxSize().toString(), dto.getVideoDescription());
        
        // 更新其他文件限制
        configurationService.updateByModuleAndKey("file", "other_max_size", dto.getOtherMaxSize().toString(), dto.getOtherDescription());
        
        // 清空缓存
        fileSizeLimitCache.clearCache();
        
        return getFileLimits();
    }
}
