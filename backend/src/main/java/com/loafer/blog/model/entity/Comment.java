package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("post_id")
    private Long postId;
    @TableField("user_id")
    private Long userId;
    @TableField("parent_id")
    private Long parentId;
    private String content;
    @TableField("original_content")
    private String originalContent;
    @TableField("created_at")
    private LocalDateTime createTime;
    @TableField("updated_at")
    private LocalDateTime updateTime;
    private Integer status;
}