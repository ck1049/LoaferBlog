package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.entity.PostTag;
import com.loafer.blog.entity.Tag;
import com.loafer.blog.mapper.PostTagMapper;
import com.loafer.blog.mapper.TagMapper;
import com.loafer.blog.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final PostTagMapper postTagMapper;

    public TagServiceImpl(PostTagMapper postTagMapper) {
        this.postTagMapper = postTagMapper;
    }

    @Override
    public List<Tag> getAllTags() {
        return baseMapper.selectList(null);
    }

    @Override
    public Tag createTag(Tag tag) {
        baseMapper.insert(tag);
        return tag;
    }

    @Override
    public Tag updateTag(Tag tag) {
        baseMapper.updateById(tag);
        return tag;
    }

    @Override
    public boolean deleteTag(Long id) {
        // 先删除标签与帖子的关联
        QueryWrapper<PostTag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_id", id);
        postTagMapper.delete(wrapper);
        // 再删除标签
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public List<Tag> getTagsByPostId(Long postId) {
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
        
        return baseMapper.selectBatchIds(tagIds);
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
