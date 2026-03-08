package com.loafer.blog.model.dto;

import com.loafer.blog.model.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostWithTimeDTO {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String summary;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime interactionTime; // 用于存储点赞、收藏或浏览时间
    private LocalDateTime likedAt; // 点赞时间
    private LocalDateTime favoritedAt; // 收藏时间
    private LocalDateTime viewedAt; // 浏览时间

    public PostWithTimeDTO(Post post, LocalDateTime interactionTime) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getAuthorId();
        this.summary = post.getSummary();
        this.viewCount = post.getViewCount();
        this.commentCount = post.getCommentCount();
        this.likeCount = post.getLikeCount();
        this.status = post.getStatus();
        this.createdAt = post.getCreateTime();
        this.updatedAt = post.getUpdateTime();
        this.interactionTime = interactionTime;
        this.likedAt = interactionTime;
        this.favoritedAt = interactionTime;
        this.viewedAt = interactionTime;
    }
}
