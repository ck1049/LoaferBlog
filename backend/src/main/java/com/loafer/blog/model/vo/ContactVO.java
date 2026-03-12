package com.loafer.blog.model.vo;

import com.loafer.blog.model.entity.Message;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactVO {
    private Long userId;
    private UserVO user;
    private Message lastMessage;
    private LocalDateTime lastMessageTime;
}
