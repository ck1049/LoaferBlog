package com.loafer.blog.controller;

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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<FileSizeLimitDTO> getFileLimits() {
        FileSizeLimitDTO dto = new FileSizeLimitDTO();
        String maxSizeStr = configurationService.getValueByModuleAndKey("file", "max_size", "10485760");
        dto.setMaxSize(Long.parseLong(maxSizeStr));
        dto.setDescription("文件上传最大大小（字节）");
        return ResponseVO.success(dto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<FileSizeLimitDTO> updateFileLimits(@RequestBody FileSizeLimitDTO dto) {
        configurationService.updateByModuleAndKey("file", "max_size", dto.getMaxSize().toString(), dto.getDescription());
        return getFileLimits();
    }
}
