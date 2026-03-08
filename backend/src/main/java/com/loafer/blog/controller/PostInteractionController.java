package com.loafer.blog.controller;

import com.loafer.blog.service.PostInteractionService;
import com.loafer.blog.model.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostInteractionController {

    @Autowired
    private PostInteractionService postInteractionService;

    // 点赞帖子
    @PostMapping("/{postId}/like")
    public ResponseVO<Void> likePost(@RequestAttribute("userId") Long userId, @PathVariable Long postId) {
        return postInteractionService.likePost(userId, postId);
    }

    // 取消点赞
    @DeleteMapping("/{postId}/like")
    public ResponseVO<Void> unlikePost(@RequestAttribute("userId") Long userId, @PathVariable Long postId) {
        return postInteractionService.unlikePost(userId, postId);
    }

    // 收藏帖子
    @PostMapping("/{postId}/favorite")
    public ResponseVO<Void> favoritePost(@RequestAttribute("userId") Long userId, @PathVariable Long postId) {
        return postInteractionService.favoritePost(userId, postId);
    }

    // 取消收藏
    @DeleteMapping("/{postId}/favorite")
    public ResponseVO<Void> unfavoritePost(@RequestAttribute("userId") Long userId, @PathVariable Long postId) {
        return postInteractionService.unfavoritePost(userId, postId);
    }

    // 检查用户是否已点赞帖子
    @GetMapping("/{postId}/like/check")
    public ResponseVO<Boolean> checkLiked(@RequestAttribute("userId") Long userId, @PathVariable Long postId) {
        return postInteractionService.checkLiked(userId, postId);
    }

    // 检查用户是否已收藏帖子
    @GetMapping("/{postId}/favorite/check")
    public ResponseVO<Boolean> checkFavorited(@RequestAttribute("userId") Long userId, @PathVariable Long postId) {
        return postInteractionService.checkFavorited(userId, postId);
    }
    
    // 记录帖子浏览历史
    @PostMapping("/{postId}/view")
    public ResponseVO<Void> recordViewHistory(@RequestAttribute("userId") Long userId, @PathVariable Long postId) {
        return postInteractionService.recordViewHistory(userId, postId);
    }
}
