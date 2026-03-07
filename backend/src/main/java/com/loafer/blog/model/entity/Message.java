package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("sender_id")
    private Long senderId;
    @TableField("receiver_id")
    private Long receiverId;
    private String content;
    @TableField("original_content")
    private String originalContent;
    @TableField("created_at")
    private LocalDateTime createTime;
    @TableField("updated_at")
    private LocalDateTime updateTime;
    private Integer status;
}