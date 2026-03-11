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
    @TableField("message_type")
    private Integer messageType; // 1:文本, 2:表情, 3:图片, 4:视频, 5:文件
    @TableField("file_path")
    private String filePath; // 存储文件路径
    @TableField("file_name")
    private String fileName; // 存储文件名
    @TableField("file_size")
    private Long fileSize; // 存储文件大小
    @TableField("send_status")
    private Integer sendStatus; // 1:发送成功, 0:发送失败
    @TableField("error_message")
    private String errorMessage; // 发送失败原因
    @TableField("is_top")
    private Integer isTop; // 1:置顶, 0:普通
    @TableField("created_at")
    private LocalDateTime createTime;
    @TableField("updated_at")
    private LocalDateTime updateTime;
    private Integer status;
}