package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.entity.Comment;
import com.loafer.blog.model.vo.UserVO;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<Comment> getCommentsByPostIdWithPagination(Long postId, Long parentId, Long lastCommentId, int size);
    int getCommentsCountByPostId(Long postId, Long parentId);
    Comment createComment(Comment comment);
    boolean deleteComment(Long id, UserVO userVO);
    List<Comment> getRepliesByCommentId(Long commentId);
    List<Comment> getCommentsByTopLevelIdWithPagination(Long postId, Long topLevelId, Long lastCommentId, int size);
    int getCommentsCountByTopLevelId(Long postId, Long topLevelId);
}
