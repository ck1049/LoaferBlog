package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    List<Tag> getAllTags();
    Tag createTag(Tag tag);
    Tag updateTag(Tag tag);
    boolean deleteTag(Long id);
    List<Tag> getTagsByPostId(Long postId);
    void addTagToPost(Long postId, List<Long> tagIds);
}
