package com.loafer.blog.controller;

import com.loafer.blog.model.entity.Comment;
import com.loafer.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }



    @GetMapping("/post/{postId}/pagination")
    public ResponseEntity<List<Comment>> getCommentsByPostIdWithPagination(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") Long parentId,
            @RequestParam(required = false) Long lastCommentId,
            @RequestParam(defaultValue = "5") int size) {
        List<Comment> comments = commentService.getCommentsByPostIdWithPagination(postId, parentId, lastCommentId, size);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Integer> getCommentsCountByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") Long parentId) {
        int count = commentService.getCommentsCountByPostId(postId, parentId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/replies/{commentId}")
    public ResponseEntity<List<Comment>> getRepliesByCommentId(@PathVariable Long commentId) {
        List<Comment> replies = commentService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @RequestAttribute("userId") Long userId) {
        comment.setUserId(userId);
        Comment createdComment = commentService.createComment(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        boolean deleted = commentService.deleteComment(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
