package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    private Long authorId;
    private String summary;
    private Integer viewCount;
    private Integer commentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status;
}