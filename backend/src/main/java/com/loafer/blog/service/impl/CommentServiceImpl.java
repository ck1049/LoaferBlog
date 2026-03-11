package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.model.entity.Comment;
import com.loafer.blog.model.entity.Post;
import com.loafer.blog.mapper.CommentMapper;
import com.loafer.blog.mapper.PostMapper;
import com.loafer.blog.service.CommentService;
import com.loafer.blog.common.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final SensitiveWordFilter sensitiveWordFilter;
    
    @Autowired
    private PostMapper postMapper;

    public CommentServiceImpl(SensitiveWordFilter sensitiveWordFilter) {
        this.sensitiveWordFilter = sensitiveWordFilter;
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId)
                .eq("parent_id", 0)
                .orderByAsc("create_time");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Comment> getCommentsByPostIdWithPagination(Long postId, Long parentId, Long lastCommentId, int size) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId)
                .eq("parent_id", parentId)
                .orderByDesc("create_time");
        
        // 如果提供了lastCommentId，则查询ID小于该值的记录
        if (lastCommentId != null) {
            wrapper.lt("id", lastCommentId);
        }
        
        // 限制返回记录数
        wrapper.last("LIMIT " + size);
        
        return baseMapper.selectList(wrapper);
    }

    @Override
    public int getCommentsCountByPostId(Long postId, Long parentId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId)
                .eq("parent_id", parentId);
        return baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public Comment createComment(Comment comment) {
        // 敏感词过滤
        String originalContent = comment.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        comment.setOriginalContent(originalContent);
        comment.setContent(filteredContent);
        if (comment.getParentId() == null) {
            comment.setParentId(0L);
        }

        baseMapper.insert(comment);
        
        // 更新帖子评论数
        if (comment.getParentId() == 0) { // 只统计顶级评论
            Post post = postMapper.selectById(comment.getPostId());
            if (post != null) {
                UpdateWrapper<Post> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", comment.getPostId())
                        .set("comment_count", post.getCommentCount() != null ? post.getCommentCount() + 1 : 1);
                postMapper.update(null, wrapper);
            }
        }
        
        return comment;
    }

    @Override
    public boolean deleteComment(Long id) {
        // 获取评论信息
        Comment comment = baseMapper.selectById(id);
        if (comment == null) {
            return false;
        }
        
        // 先删除回复
        QueryWrapper<Comment> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("parent_id", id);
        baseMapper.delete(replyWrapper);
        
        // 再删除评论
        boolean result = baseMapper.deleteById(id) > 0;
        
        // 更新帖子评论数
        if (result && comment.getParentId() == 0) { // 只统计顶级评论
            Post post = postMapper.selectById(comment.getPostId());
            if (post != null) {
                UpdateWrapper<Post> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", comment.getPostId())
                        .set("comment_count", Math.max(0, post.getCommentCount() != null ? post.getCommentCount() - 1 : 0));
                postMapper.update(null, wrapper);
            }
        }
        
        return result;
    }

    @Override
    public List<Comment> getRepliesByCommentId(Long commentId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", commentId)
                .orderByAsc("create_time");
        return baseMapper.selectList(wrapper);
    }
}
