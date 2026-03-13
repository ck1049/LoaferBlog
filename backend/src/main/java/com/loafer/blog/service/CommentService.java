package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<Comment> getCommentsByPostIdWithPagination(Long postId, Long parentId, Long lastCommentId, int size);
    int getCommentsCountByPostId(Long postId, Long parentId);
    Comment createComment(Comment comment);
    boolean deleteComment(Long id);
    List<Comment> getRepliesByCommentId(Long commentId);
}
