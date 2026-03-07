package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.entity.Message;
import com.loafer.blog.mapper.MessageMapper;
import com.loafer.blog.service.MessageService;
import com.loafer.blog.utils.SensitiveWordFilter;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orderByDesc("created_at");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Message createMessage(Message message) {
        // 敏感词过滤
        String originalContent = message.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        message.setFilteredContent(filteredContent);

        baseMapper.insert(message);
        return message;
    }

    @Override
    public Message replyMessage(Message message) {
        // 敏感词过滤
        String originalContent = message.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        message.setFilteredContent(filteredContent);

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
}
