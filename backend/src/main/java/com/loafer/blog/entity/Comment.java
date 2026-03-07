package com.loafer.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private Long parentId;
    private Long userId;
    private String content;
    private String filteredContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean status;
}