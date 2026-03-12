package com.loafer.blog.controller;

import com.loafer.blog.model.dto.FileSizeLimitDTO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.service.FileLimitService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/file-limits")
public class FileLimitController {

    private final FileLimitService fileLimitService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<FileSizeLimitDTO> getFileLimits() {
        return ResponseVO.success(fileLimitService.getFileLimits());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<FileSizeLimitDTO> updateFileLimits(@RequestBody FileSizeLimitDTO dto) {
        return ResponseVO.success(fileLimitService.updateFileLimits(dto));
    }
}
