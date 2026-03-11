package com.loafer.blog.controller;

import com.loafer.blog.model.vo.ResponseVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/file-limits")
public class FileLimitController {

    // 模拟文件大小限制存储
    // 实际项目中应该从配置或数据库中获取
    private Map<String, Integer> fileLimits = new HashMap<>() {
        {
            put("image", 10); // 默认10MB
            put("video", 500); // 默认500MB
            put("other", 1000); // 默认1000MB
        }
    };

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<Map<String, Integer>> getFileLimits() {
        return ResponseVO.success(fileLimits);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<Map<String, Integer>> updateFileLimits(@RequestBody Map<String, Integer> newLimits) {
        // 更新文件大小限制
        if (newLimits.containsKey("image")) {
            fileLimits.put("image", newLimits.get("image"));
        }
        if (newLimits.containsKey("video")) {
            fileLimits.put("video", newLimits.get("video"));
        }
        if (newLimits.containsKey("other")) {
            fileLimits.put("other", newLimits.get("other"));
        }
        return ResponseVO.success(fileLimits);
    }
}
