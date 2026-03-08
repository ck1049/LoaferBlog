package com.loafer.blog.model.vo;

import com.loafer.blog.model.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostVO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CategoryVO> categories;
    private List<TagVO> tags;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;

    public PostVO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreateTime();
        this.updatedAt = post.getUpdateTime();
        this.viewCount = post.getViewCount();
        this.commentCount = post.getCommentCount();
        this.likeCount = post.getLikeCount();
    }
}
