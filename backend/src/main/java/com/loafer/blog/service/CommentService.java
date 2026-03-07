package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {
    List<Comment> getCommentsByPostId(Long postId);
    Comment createComment(Comment comment);
    boolean deleteComment(Long id);
    List<Comment> getRepliesByCommentId(Long commentId);
}
