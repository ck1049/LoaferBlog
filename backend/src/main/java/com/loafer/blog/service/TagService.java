package com.loafer.blog.service;

import com.loafer.blog.model.dto.TagDTO;
import com.loafer.blog.model.vo.TagVO;

import java.util.List;

public interface TagService {
    List<TagVO> getAllTags();
    TagVO createTag(TagDTO tagDTO);
    TagVO updateTag(Long id, TagDTO tagDTO);
    boolean deleteTag(Long id);
    List<TagVO> getTagsByPostId(Long postId);
    void addTagToPost(Long postId, List<Long> tagIds);
}
