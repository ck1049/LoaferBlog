package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
    @TableField(exist = false)
    private Integer favoriteCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    private Integer status;
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
    private LocalDateTime deleteTime;
}