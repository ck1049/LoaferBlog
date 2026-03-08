package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.model.dto.TagDTO;
import com.loafer.blog.model.entity.PostTag;
import com.loafer.blog.model.entity.Tag;
import com.loafer.blog.model.vo.TagVO;
import com.loafer.blog.mapper.PostTagMapper;
import com.loafer.blog.mapper.TagMapper;
import com.loafer.blog.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;
    private final PostTagMapper postTagMapper;

    public TagServiceImpl(TagMapper tagMapper, PostTagMapper postTagMapper) {
        this.tagMapper = tagMapper;
        this.postTagMapper = postTagMapper;
    }

    @Override
    public List<TagVO> getAllTags() {
        List<Tag> tags = tagMapper.selectList(null);
        return tags.stream()
                .map(TagVO::new)
                .collect(Collectors.toList());
    }

    @Override
    public TagVO createTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        tag.setDescription(tagDTO.getDescription());
        tag.setStatus(tagDTO.getStatus());
        tagMapper.insert(tag);
        return new TagVO(tag);
    }

    @Override
    public TagVO updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new RuntimeException("标签不存在");
        }
        tag.setName(tagDTO.getName());
        tag.setDescription(tagDTO.getDescription());
        tag.setStatus(tagDTO.getStatus());
        tagMapper.updateById(tag);
        return new TagVO(tag);
    }

    @Override
    public boolean deleteTag(Long id) {
        // 先删除标签与帖子的关联
        QueryWrapper<PostTag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_id", id);
        postTagMapper.delete(wrapper);
        // 再删除标签
        return tagMapper.deleteById(id) > 0;
    }

    @Override
    public List<TagVO> getTagsByPostId(Long postId) {
        // 通过PostTag关联查询标签
        QueryWrapper<PostTag> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId);
        List<PostTag> postTags = postTagMapper.selectList(wrapper);
        
        List<Long> tagIds = postTags.stream()
                .map(PostTag::getTagId)
                .toList();
        
        if (tagIds.isEmpty()) {
            return List.of();
        }
        
        List<Tag> tags = tagMapper.selectBatchIds(tagIds);
        return tags.stream()
                .map(TagVO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void addTagToPost(Long postId, List<Long> tagIds) {
        // 先删除现有关联
        QueryWrapper<PostTag> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId);
        postTagMapper.delete(wrapper);
        
        // 添加新关联
        for (Long tagId : tagIds) {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            postTagMapper.insert(postTag);
        }
    }
}
