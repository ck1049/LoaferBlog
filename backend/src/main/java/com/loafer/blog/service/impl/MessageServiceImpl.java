package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.model.entity.Message;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.model.vo.ContactVO;
import com.loafer.blog.mapper.MessageMapper;
import com.loafer.blog.mapper.UserMapper;
import com.loafer.blog.model.vo.MessageVO;
import com.loafer.blog.model.vo.UserVO;
import com.loafer.blog.service.MessageService;
import com.loafer.blog.common.SensitiveWordFilter;
import com.loafer.blog.model.enumtype.MessageType;
import com.loafer.blog.utils.FileUploadUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.LocalDateTime;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final SensitiveWordFilter sensitiveWordFilter;
    private final UserMapper userMapper;

    public MessageServiceImpl(SensitiveWordFilter sensitiveWordFilter, UserMapper userMapper) {
        this.sensitiveWordFilter = sensitiveWordFilter;
        this.userMapper = userMapper;
    }

    @Override
    public List<Message> getMessagesByReceiverId(Long receiverId) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver_id", receiverId)
                .orderByDesc("is_top")
                .orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Message createMessage(Message message) {
        // 敏感词过滤
        String originalContent = message.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        message.setOriginalContent(originalContent);
        message.setContent(filteredContent);
        message.setMessageType(MessageType.TEXT.getCode()); // 默认文本消息
        message.setSendStatus(1); // 默认发送成功
        message.setIsTop(0); // 默认非置顶

        baseMapper.insert(message);
        return message;
    }

    @Override
    public MessageVO replyMessage(Message message) {
        // 敏感词过滤
        String originalContent = message.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        message.setOriginalContent(originalContent);
        message.setContent(filteredContent);
        message.setMessageType(MessageType.TEXT.getCode()); // 默认文本消息
        message.setSendStatus(1); // 默认发送成功

        baseMapper.insert(message);
        
        // 转换为MessageVO并设置sender和receiver
        MessageVO messageVO = new MessageVO(message);
        
        // 设置发送者信息
        User sender = userMapper.selectById(message.getSenderId());
        if (sender != null) {
            // 为头像地址拼接域名前缀
            String avatar = sender.getAvatar();
            if (avatar != null && !avatar.startsWith("http")) {
                sender.setAvatar(FileUploadUtils.spliceUrl(avatar));
            }
            messageVO.setSender(new UserVO(sender));
        }
        
        // 设置接收者信息
        User receiver = userMapper.selectById(message.getReceiverId());
        if (receiver != null) {
            // 为头像地址拼接域名前缀
            String avatar = receiver.getAvatar();
            if (avatar != null && !avatar.startsWith("http")) {
                receiver.setAvatar(FileUploadUtils.spliceUrl(avatar));
            }
            messageVO.setReceiver(new UserVO(receiver));
        }
        
        return messageVO;
    }

    @Override
    public List<MessageVO> getMessageHistory(Long userId1, Long userId2) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.and(w -> w.eq("sender_id", userId1).eq("receiver_id", userId2))
                .or(w -> w.eq("sender_id", userId2).eq("receiver_id", userId1))
                .orderByAsc("create_time");
        List<Message> messages = baseMapper.selectList(wrapper);
        
        // 转换为MessageVO并设置sender和receiver
        List<MessageVO> messageVOs = new ArrayList<>();
        for (Message message : messages) {
            MessageVO messageVO = new MessageVO(message);
            
            // 设置发送者信息
        User sender = userMapper.selectById(message.getSenderId());
        if (sender != null) {
            // 为头像地址拼接域名前缀
            String avatar = sender.getAvatar();
            if (avatar != null && !avatar.startsWith("http")) {
                sender.setAvatar(FileUploadUtils.spliceUrl(avatar));
            }
            messageVO.setSender(new UserVO(sender));
        }
        
        // 设置接收者信息
        User receiver = userMapper.selectById(message.getReceiverId());
        if (receiver != null) {
            // 为头像地址拼接域名前缀
            String avatar = receiver.getAvatar();
            if (avatar != null && !avatar.startsWith("http")) {
                receiver.setAvatar(FileUploadUtils.spliceUrl(avatar));
            }
            messageVO.setReceiver(new UserVO(receiver));
        }
            
            messageVOs.add(messageVO);
        }
        
        return messageVOs;
    }

    @Override
    public List<ContactVO> getContactList(Long userId) {
        // 获取所有与该用户有消息往来的用户ID
        List<Message> messages = baseMapper.selectList(new QueryWrapper<Message>()
                .and(w -> w.eq("sender_id", userId).or().eq("receiver_id", userId)));

        Map<Long, ContactVO> contactMap = new HashMap<>();

        for (Message message : messages) {
            Long contactId = message.getSenderId().equals(userId) ? message.getReceiverId() : message.getSenderId();
            
            if (!contactMap.containsKey(contactId)) {
                ContactVO contactVO = new ContactVO();
                contactVO.setUserId(contactId);
                
                // 获取用户信息
                User user = userMapper.selectById(contactId);
                if (user != null) {
                    // 为头像地址拼接域名前缀
                    String avatar = user.getAvatar();
                    if (avatar != null && !avatar.startsWith("http")) {
                        user.setAvatar(FileUploadUtils.spliceUrl(avatar));
                    }
                    contactVO.setUser(new UserVO(user));
                }
                
                contactVO.setLastMessage(message);
                contactVO.setLastMessageTime(message.getCreateTime());
                contactMap.put(contactId, contactVO);
            } else {
                // 更新最新消息
                ContactVO existingInfo = contactMap.get(contactId);
                LocalDateTime existingTime = existingInfo.getLastMessageTime();
                LocalDateTime currentTime = message.getCreateTime();
                
                if (currentTime.isAfter(existingTime)) {
                    existingInfo.setLastMessage(message);
                    existingInfo.setLastMessageTime(currentTime);
                }
            }
        }

        // 转换为列表并按时间排序
        List<ContactVO> contactList = new ArrayList<>(contactMap.values());
        contactList.sort((a, b) -> {
            LocalDateTime timeA = a.getLastMessageTime();
            LocalDateTime timeB = b.getLastMessageTime();
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
    public MessageVO sendFileMessage(Message message) {
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
        
        // 转换为MessageVO并设置sender和receiver
        MessageVO messageVO = new MessageVO(message);
        
        // 设置发送者信息
        User sender = userMapper.selectById(message.getSenderId());
        if (sender != null) {
            // 为头像地址拼接域名前缀
            String avatar = sender.getAvatar();
            if (avatar != null && !avatar.startsWith("http")) {
                sender.setAvatar(FileUploadUtils.spliceUrl(avatar));
            }
            messageVO.setSender(new UserVO(sender));
        }
        
        // 设置接收者信息
        User receiver = userMapper.selectById(message.getReceiverId());
        if (receiver != null) {
            // 为头像地址拼接域名前缀
            String avatar = receiver.getAvatar();
            if (avatar != null && !avatar.startsWith("http")) {
                receiver.setAvatar(FileUploadUtils.spliceUrl(avatar));
            }
            messageVO.setReceiver(new UserVO(receiver));
        }
        return messageVO;
    }
}
