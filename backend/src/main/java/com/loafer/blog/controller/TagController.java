package com.loafer.blog.controller;

import com.loafer.blog.model.entity.Tag;
import com.loafer.blog.service.TagService;
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
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Tag>> getTagsByPostId(@PathVariable Long postId) {
        List<Tag> tags = tagService.getTagsByPostId(postId);
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag createdTag = tagService.createTag(tag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        Tag updatedTag = tagService.updateTag(tag);
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
