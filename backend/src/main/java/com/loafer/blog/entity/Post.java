package com.loafer.blog.entity;

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
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer viewCount;
    private Boolean status;
}