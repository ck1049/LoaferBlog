package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.loafer.blog.mapper.PostLikeMapper;
import com.loafer.blog.mapper.PostFavoriteMapper;
import com.loafer.blog.mapper.PostMapper;
import com.loafer.blog.mapper.PostViewHistoryMapper;
import com.loafer.blog.model.entity.Post;
import com.loafer.blog.model.entity.PostLike;
import com.loafer.blog.model.entity.PostFavorite;
import com.loafer.blog.model.entity.PostViewHistory;
import com.loafer.blog.model.dto.PostWithTimeDTO;
import com.loafer.blog.service.PostInteractionService;
import com.loafer.blog.model.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostInteractionServiceImpl implements PostInteractionService {

    @Autowired
    private PostLikeMapper postLikeMapper;
    
    @Autowired
    private PostFavoriteMapper postFavoriteMapper;
    
    @Autowired
    private PostViewHistoryMapper postViewHistoryMapper;
    
    @Autowired
    private PostMapper postMapper;

    @Override
    public ResponseVO<Void> likePost(Long userId, Long postId) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return ResponseVO.error("帖子不存在");
        }
        
        // 检查是否已经点赞
        QueryWrapper<PostLike> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId).eq("user_id", userId);
        if (postLikeMapper.selectOne(wrapper) != null) {
            return ResponseVO.error("已经点赞过该帖子");
        }
        
        // 添加点赞记录
        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLike.setCreateTime(LocalDateTime.now());
        postLikeMapper.insert(postLike);
        
        // 更新帖子点赞数
        UpdateWrapper<Post> postUpdateWrapper = new UpdateWrapper<>();
        postUpdateWrapper.eq("id", postId)
                .set("like_count", post.getLikeCount() != null ? post.getLikeCount() + 1 : 1);
        postMapper.update(null, postUpdateWrapper);
        
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<Void> unlikePost(Long userId, Long postId) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return ResponseVO.error("帖子不存在");
        }
        
        // 检查是否已经点赞
        QueryWrapper<PostLike> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId).eq("user_id", userId);
        PostLike postLike = postLikeMapper.selectOne(wrapper);
        if (postLike == null) {
            return ResponseVO.error("未点赞过该帖子");
        }
        
        // 删除点赞记录
        postLikeMapper.delete(wrapper);
        
        // 更新帖子点赞数
        UpdateWrapper<Post> postUpdateWrapper = new UpdateWrapper<>();
        postUpdateWrapper.eq("id", postId)
                .set("like_count", Math.max(0, post.getLikeCount() != null ? post.getLikeCount() - 1 : 0));
        postMapper.update(null, postUpdateWrapper);
        
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<Void> favoritePost(Long userId, Long postId) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return ResponseVO.error("帖子不存在");
        }
        
        // 检查是否已经收藏
        QueryWrapper<PostFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId).eq("user_id", userId);
        if (postFavoriteMapper.selectOne(wrapper) != null) {
            return ResponseVO.error("已经收藏过该帖子");
        }
        
        // 添加收藏记录
        PostFavorite postFavorite = new PostFavorite();
        postFavorite.setPostId(postId);
        postFavorite.setUserId(userId);
        postFavorite.setCreateTime(LocalDateTime.now());
        postFavoriteMapper.insert(postFavorite);
        
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<Void> unfavoritePost(Long userId, Long postId) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return ResponseVO.error("帖子不存在");
        }
        
        // 检查是否已经收藏
        QueryWrapper<PostFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId).eq("user_id", userId);
        PostFavorite postFavorite = postFavoriteMapper.selectOne(wrapper);
        if (postFavorite == null) {
            return ResponseVO.error("未收藏过该帖子");
        }
        
        // 删除收藏记录
        postFavoriteMapper.delete(wrapper);
        
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<Boolean> checkLiked(Long userId, Long postId) {
        QueryWrapper<PostLike> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId).eq("user_id", userId);
        PostLike postLike = postLikeMapper.selectOne(wrapper);
        return ResponseVO.success(postLike != null);
    }

    @Override
    public ResponseVO<Boolean> checkFavorited(Long userId, Long postId) {
        QueryWrapper<PostFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId).eq("user_id", userId);
        PostFavorite postFavorite = postFavoriteMapper.selectOne(wrapper);
        return ResponseVO.success(postFavorite != null);
    }

    @Override
    public ResponseVO<?> getLikedPosts(Long userId, Integer page, Integer size) {
        QueryWrapper<PostLike> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("create_time");
        List<PostLike> postLikes = postLikeMapper.selectList(wrapper);
        
        // 构建返回数据，包含帖子信息和点赞时间
        List<PostWithTimeDTO> result = new ArrayList<>();
        
        if (!postLikes.isEmpty()) {
            // 获取帖子ID列表
            List<Long> postIds = postLikes.stream()
                    .map(PostLike::getPostId)
                    .collect(Collectors.toList());
            
            // 获取帖子信息
            List<Post> posts = postMapper.selectBatchIds(postIds);
            
            result = postLikes.stream()
                    .map(like -> {
                        Post post = posts.stream()
                                .filter(p -> p.getId().equals(like.getPostId()))
                                .findFirst()
                                .orElse(null);
                        if (post != null) {
                            return new PostWithTimeDTO(post, like.getCreateTime());
                        }
                        return null;
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        }
        
        // 分页处理
        int total = result.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        List<PostWithTimeDTO> paginatedResult = new ArrayList<>();
        if (start < total) {
            paginatedResult = result.subList(start, end);
        }
        
        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("list", paginatedResult);
        data.put("total", total);
        data.put("page", page);
        data.put("size", size);
        
        return ResponseVO.success(data);
    }

    @Override
    public ResponseVO<?> getFavoritedPosts(Long userId, Integer page, Integer size) {
        QueryWrapper<PostFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("create_time");
        List<PostFavorite> postFavorites = postFavoriteMapper.selectList(wrapper);
        
        // 构建返回数据，包含帖子信息和收藏时间
        List<PostWithTimeDTO> result = new ArrayList<>();
        
        if (!postFavorites.isEmpty()) {
            // 获取帖子ID列表
            List<Long> postIds = postFavorites.stream()
                    .map(PostFavorite::getPostId)
                    .collect(Collectors.toList());
            
            // 获取帖子信息
            List<Post> posts = postMapper.selectBatchIds(postIds);
            
            result = postFavorites.stream()
                    .map(favorite -> {
                        Post post = posts.stream()
                                .filter(p -> p.getId().equals(favorite.getPostId()))
                                .findFirst()
                                .orElse(null);
                        if (post != null) {
                            return new PostWithTimeDTO(post, favorite.getCreateTime());
                        }
                        return null;
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        }
        
        // 分页处理
        int total = result.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        List<PostWithTimeDTO> paginatedResult = new ArrayList<>();
        if (start < total) {
            paginatedResult = result.subList(start, end);
        }
        
        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("list", paginatedResult);
        data.put("total", total);
        data.put("page", page);
        data.put("size", size);
        
        return ResponseVO.success(data);
    }

    @Override
    public ResponseVO<Void> recordViewHistory(Long userId, Long postId) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return ResponseVO.error("帖子不存在");
        }
        
        // 检查是否已经记录过浏览历史
        QueryWrapper<PostViewHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId).eq("user_id", userId);
        PostViewHistory existingHistory = postViewHistoryMapper.selectOne(wrapper);
        
        if (existingHistory != null) {
            // 更新浏览时间
            existingHistory.setViewedAt(LocalDateTime.now());
            postViewHistoryMapper.updateById(existingHistory);
        } else {
            // 添加浏览历史记录
            PostViewHistory viewHistory = new PostViewHistory();
            viewHistory.setPostId(postId);
            viewHistory.setUserId(userId);
            viewHistory.setViewedAt(LocalDateTime.now());
            postViewHistoryMapper.insert(viewHistory);
        }
        
        // 更新帖子浏览数
        UpdateWrapper<Post> postUpdateWrapper = new UpdateWrapper<>();
        postUpdateWrapper.eq("id", postId)
                .set("view_count", post.getViewCount() != null ? post.getViewCount() + 1 : 1);
        postMapper.update(null, postUpdateWrapper);
        
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<?> getViewHistory(Long userId, Integer page, Integer size) {
        QueryWrapper<PostViewHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("viewed_at");
        List<PostViewHistory> viewHistories = postViewHistoryMapper.selectList(wrapper);
        
        // 构建返回数据，包含帖子信息和浏览时间
        List<PostWithTimeDTO> result = new ArrayList<>();
        
        if (!viewHistories.isEmpty()) {
            // 获取帖子ID列表
            List<Long> postIds = viewHistories.stream()
                    .map(PostViewHistory::getPostId)
                    .collect(Collectors.toList());
            
            // 获取帖子信息
            List<Post> posts = postMapper.selectBatchIds(postIds);
            
            result = viewHistories.stream()
                    .map(history -> {
                        Post post = posts.stream()
                                .filter(p -> p.getId().equals(history.getPostId()))
                                .findFirst()
                                .orElse(null);
                        if (post != null) {
                            return new PostWithTimeDTO(post, history.getViewedAt());
                        }
                        return null;
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        }
        
        // 分页处理
        int total = result.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        List<PostWithTimeDTO> paginatedResult = new ArrayList<>();
        if (start < total) {
            paginatedResult = result.subList(start, end);
        }
        
        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("list", paginatedResult);
        data.put("total", total);
        data.put("page", page);
        data.put("size", size);
        
        return ResponseVO.success(data);
    }

    @Override
    public ResponseVO<Void> deleteViewHistory(Long userId, Long postId) {
        QueryWrapper<PostViewHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("post_id", postId);
        int result = postViewHistoryMapper.delete(wrapper);
        if (result > 0) {
            return ResponseVO.success();
        } else {
            return ResponseVO.error("删除浏览历史失败");
        }
    }
}
