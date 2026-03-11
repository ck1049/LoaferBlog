package com.loafer.blog.controller;

import com.loafer.blog.model.dto.TagDTO;
import com.loafer.blog.model.vo.TagVO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.service.TagService;
import jakarta.validation.Valid;
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
    public ResponseVO<List<TagVO>> getAllTags() {
        List<TagVO> tags = tagService.getAllTags();
        return ResponseVO.success(tags);
    }

    @GetMapping("/post/{postId}")
    public ResponseVO<List<TagVO>> getTagsByPostId(@PathVariable Long postId) {
        List<TagVO> tags = tagService.getTagsByPostId(postId);
        return ResponseVO.success(tags);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<TagVO> createTag(@Valid @RequestBody TagDTO tagDTO) {
        TagVO createdTag = tagService.createTag(tagDTO);
        return ResponseVO.success(createdTag);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<TagVO> updateTag(@PathVariable Long id, @Valid @RequestBody TagDTO tagDTO) {
        TagVO updatedTag = tagService.updateTag(id, tagDTO);
        return ResponseVO.success(updatedTag);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<Void> deleteTag(@PathVariable Long id) {
        boolean deleted = tagService.deleteTag(id);
        if (deleted) {
            return ResponseVO.success();
        } else {
            return ResponseVO.error("标签不存在");
        }
    }

    @PostMapping("/post/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<Void> addTagToPost(@PathVariable Long postId, @RequestBody List<Long> tagIds) {
        tagService.addTagToPost(postId, tagIds);
        return ResponseVO.success();
    }
}
