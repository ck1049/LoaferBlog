package com.loafer.blog.service;

import com.loafer.blog.model.vo.ResponseVO;

public interface PostInteractionService {
    // 点赞帖子
    ResponseVO<Void> likePost(Long userId, Long postId);
    
    // 取消点赞
    ResponseVO<Void> unlikePost(Long userId, Long postId);
    
    // 收藏帖子
    ResponseVO<Void> favoritePost(Long userId, Long postId);
    
    // 取消收藏
    ResponseVO<Void> unfavoritePost(Long userId, Long postId);
    
    // 检查用户是否已点赞帖子
    ResponseVO<Boolean> checkLiked(Long userId, Long postId);
    
    // 检查用户是否已收藏帖子
    ResponseVO<Boolean> checkFavorited(Long userId, Long postId);
    
    // 获取用户的点赞列表
    ResponseVO<?> getLikedPosts(Long userId, Integer page, Integer size);
    
    // 获取用户的收藏列表
    ResponseVO<?> getFavoritedPosts(Long userId, Integer page, Integer size);
    
    // 记录帖子浏览历史
    ResponseVO<Void> recordViewHistory(Long userId, Long postId);
    
    // 获取用户的浏览历史
    ResponseVO<?> getViewHistory(Long userId, Integer page, Integer size);
    
    // 删除用户的浏览历史
    ResponseVO<Void> deleteViewHistory(Long userId, Long postId);
}
