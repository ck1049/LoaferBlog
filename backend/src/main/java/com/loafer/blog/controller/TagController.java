package com.loafer.blog.controller;

import com.loafer.blog.model.dto.TagDTO;
import com.loafer.blog.model.vo.TagVO;
import com.loafer.blog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagVO>> getAllTags() {
        List<TagVO> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<TagVO>> getTagsByPostId(@PathVariable Long postId) {
        List<TagVO> tags = tagService.getTagsByPostId(postId);
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagVO> createTag(@Valid @RequestBody TagDTO tagDTO) {
        TagVO createdTag = tagService.createTag(tagDTO);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagVO> updateTag(@PathVariable Long id, @Valid @RequestBody TagDTO tagDTO) {
        TagVO updatedTag = tagService.updateTag(id, tagDTO);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        boolean deleted = tagService.deleteTag(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/post/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addTagToPost(@PathVariable Long postId, @RequestBody List<Long> tagIds) {
        tagService.addTagToPost(postId, tagIds);
        return ResponseEntity.ok().build();
    }
}
