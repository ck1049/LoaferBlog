package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.model.entity.Message;
import com.loafer.blog.mapper.MessageMapper;
import com.loafer.blog.service.MessageService;
import com.loafer.blog.common.SensitiveWordFilter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final SensitiveWordFilter sensitiveWordFilter;

    public MessageServiceImpl(SensitiveWordFilter sensitiveWordFilter) {
        this.sensitiveWordFilter = sensitiveWordFilter;
    }

    @Override
    public List<Message> getMessagesByReceiverId(Long receiverId) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver_id", receiverId)
                .orderByDesc("is_top")
                .orderByDesc("created_at");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Message createMessage(Message message) {
        // 敏感词过滤
        String originalContent = message.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        message.setOriginalContent(originalContent);
        message.setContent(filteredContent);
        message.setMessageType(1); // 默认文本消息
        message.setSendStatus(1); // 默认发送成功
        message.setIsTop(0); // 默认非置顶

        baseMapper.insert(message);
        return message;
    }

    @Override
    public Message replyMessage(Message message) {
        // 敏感词过滤
        String originalContent = message.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        message.setOriginalContent(originalContent);
        message.setContent(filteredContent);
        message.setMessageType(1); // 默认文本消息
        message.setSendStatus(1); // 默认发送成功

        baseMapper.insert(message);
        return message;
    }

    @Override
    public List<Message> getMessageHistory(Long userId1, Long userId2) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.and(w -> w.eq("sender_id", userId1).eq("receiver_id", userId2))
                .or(w -> w.eq("sender_id", userId2).eq("receiver_id", userId1))
                .orderByAsc("created_at");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> getContactList(Long userId) {
        // 获取所有与该用户有消息往来的用户ID
        List<Message> messages = baseMapper.selectList(new QueryWrapper<Message>()
                .and(w -> w.eq("sender_id", userId).or().eq("receiver_id", userId)));

        Map<Long, Map<String, Object>> contactMap = new HashMap<>();

        for (Message message : messages) {
            Long contactId = message.getSenderId().equals(userId) ? message.getReceiverId() : message.getSenderId();
            
            if (!contactMap.containsKey(contactId)) {
                Map<String, Object> contactInfo = new HashMap<>();
                contactInfo.put("userId", contactId);
                contactInfo.put("lastMessage", message);
                contactInfo.put("lastMessageTime", message.getCreateTime());
                contactMap.put(contactId, contactInfo);
            } else {
                // 更新最新消息
                Map<String, Object> existingInfo = contactMap.get(contactId);
                Date existingTime = (Date) existingInfo.get("lastMessageTime");
                Date currentTime = java.sql.Timestamp.valueOf(message.getCreateTime());
                
                if (currentTime.after(existingTime)) {
                    existingInfo.put("lastMessage", message);
                    existingInfo.put("lastMessageTime", message.getCreateTime());
                }
            }
        }

        // 转换为列表并按时间排序
        List<Map<String, Object>> contactList = new ArrayList<>(contactMap.values());
        contactList.sort((a, b) -> {
            Date timeA = (Date) a.get("lastMessageTime");
            Date timeB = (Date) b.get("lastMessageTime");
            return timeB.compareTo(timeA); // 降序排列
        });

        return contactList;
    }

    @Override
    public void topMessage(Long messageId, Integer isTop) {
        Message message = baseMapper.selectById(messageId);
        if (message != null) {
            message.setIsTop(isTop);
            baseMapper.updateById(message);
        }
    }

    @Override
    public void deleteMessage(Long messageId) {
        baseMapper.deleteById(messageId);
    }

    @Override
    public Message sendFileMessage(Message message) {
        // 敏感词过滤（如果有文本内容）
        if (message.getContent() != null) {
            String originalContent = message.getContent();
            String filteredContent = sensitiveWordFilter.filter(originalContent);
            message.setOriginalContent(originalContent);
            message.setContent(filteredContent);
        }
        
        message.setSendStatus(1); // 默认发送成功
        message.setIsTop(0); // 默认非置顶

        baseMapper.insert(message);
        return message;
    }
}
