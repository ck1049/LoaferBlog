package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.model.entity.Comment;
import com.loafer.blog.mapper.CommentMapper;
import com.loafer.blog.service.CommentService;
import com.loafer.blog.common.SensitiveWordFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final SensitiveWordFilter sensitiveWordFilter;

    public CommentServiceImpl(SensitiveWordFilter sensitiveWordFilter) {
        this.sensitiveWordFilter = sensitiveWordFilter;
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId)
                .eq("parent_id", 0)
                .orderByAsc("created_at");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Comment createComment(Comment comment) {
        // 敏感词过滤
        String originalContent = comment.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        comment.setOriginalContent(originalContent);
        comment.setContent(filteredContent);

        baseMapper.insert(comment);
        return comment;
    }

    @Override
    public boolean deleteComment(Long id) {
        // 先删除回复
        QueryWrapper<Comment> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("parent_id", id);
        baseMapper.delete(replyWrapper);
        // 再删除评论
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public List<Comment> getRepliesByCommentId(Long commentId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", commentId)
                .orderByAsc("created_at");
        return baseMapper.selectList(wrapper);
    }
}
