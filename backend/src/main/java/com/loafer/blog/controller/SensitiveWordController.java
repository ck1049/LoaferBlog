package com.loafer.blog.controller;

import com.loafer.blog.model.entity.SensitiveWord;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.service.SensitiveWordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensitive-words")
public class SensitiveWordController {

    private final SensitiveWordService sensitiveWordService;

    public SensitiveWordController(SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<List<SensitiveWord>> getAllSensitiveWords() {
        List<SensitiveWord> sensitiveWords = sensitiveWordService.getAllSensitiveWords();
        return ResponseVO.success(sensitiveWords);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<SensitiveWord> createSensitiveWord(@RequestBody SensitiveWord sensitiveWord) {
        SensitiveWord createdWord = sensitiveWordService.createSensitiveWord(sensitiveWord);
        return ResponseVO.success(createdWord);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<Void> deleteSensitiveWord(@PathVariable Long id) {
        boolean deleted = sensitiveWordService.deleteSensitiveWord(id);
        if (deleted) {
            return ResponseVO.success();
        } else {
            return ResponseVO.error("敏感词不存在");
        }
    }

    @PostMapping("/reload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<Void> reloadSensitiveWords() {
        sensitiveWordService.reloadSensitiveWords();
        return ResponseVO.success();
    }
}
