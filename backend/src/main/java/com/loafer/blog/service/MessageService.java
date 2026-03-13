package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.entity.Message;
import com.loafer.blog.model.vo.MessageVO;

import java.util.List;
import java.util.Map;

public interface MessageService extends IService<Message> {
    List<Message> getMessagesByReceiverId(Long receiverId);
    Message createMessage(Message message);
    MessageVO replyMessage(Message message);
    List<MessageVO> getMessageHistory(Long userId1, Long userId2);
    List<com.loafer.blog.model.vo.ContactVO> getContactList(Long userId);
    void topMessage(Long messageId, Integer isTop);
    void deleteMessage(Long messageId);
    MessageVO sendFileMessage(Message message);
    void markMessagesAsRead(Long receiverId, Long senderId);
    List<Map<String, Object>> getUnreadCounts(Long userId);
}
