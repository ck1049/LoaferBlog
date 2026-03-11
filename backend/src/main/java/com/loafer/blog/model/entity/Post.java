package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    @TableField("author_id")
    private Long authorId;
    private String summary;
    @TableField("view_count")
    private Integer viewCount;
    @TableField("comment_count")
    private Integer commentCount;
    @TableField("like_count")
    private Integer likeCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status;
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}