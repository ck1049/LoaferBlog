package com.loafer.blog.model.vo;

import com.loafer.blog.model.entity.Message;
import com.loafer.blog.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactVO {
    private Long userId;
    private User user;
    private Message lastMessage;
    private LocalDateTime lastMessageTime;
}
