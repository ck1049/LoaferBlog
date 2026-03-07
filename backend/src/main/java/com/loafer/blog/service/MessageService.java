package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.entity.Message;

import java.util.List;

public interface MessageService extends IService<Message> {
    List<Message> getMessagesByReceiverId(Long receiverId);
    Message createMessage(Message message);
    Message replyMessage(Message message);
    List<Message> getMessageHistory(Long userId1, Long userId2);
}
