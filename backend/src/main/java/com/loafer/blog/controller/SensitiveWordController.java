package com.loafer.blog.controller;

import com.loafer.blog.entity.SensitiveWord;
import com.loafer.blog.service.SensitiveWordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<SensitiveWord>> getAllSensitiveWords() {
        List<SensitiveWord> sensitiveWords = sensitiveWordService.getAllSensitiveWords();
        return ResponseEntity.ok(sensitiveWords);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SensitiveWord> createSensitiveWord(@RequestBody SensitiveWord sensitiveWord) {
        SensitiveWord createdWord = sensitiveWordService.createSensitiveWord(sensitiveWord);
        return new ResponseEntity<>(createdWord, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSensitiveWord(@PathVariable Long id) {
        boolean deleted = sensitiveWordService.deleteSensitiveWord(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reloadSensitiveWords() {
        sensitiveWordService.reloadSensitiveWords();
        return ResponseEntity.ok().build();
    }
}
