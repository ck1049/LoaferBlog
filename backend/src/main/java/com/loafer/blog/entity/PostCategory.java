package com.loafer.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("post_category")
public class PostCategory {
    private Long postId;
    private Long categoryId;
}