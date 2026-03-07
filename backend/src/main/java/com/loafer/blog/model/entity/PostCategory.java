package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("post_category")
public class PostCategory {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("post_id")
    private Long postId;
    @TableField("category_id")
    private Long categoryId;
    @TableField("created_at")
    private LocalDateTime createTime;
}