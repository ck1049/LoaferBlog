package com.loafer.blog.model.vo;

import com.loafer.blog.model.entity.Message;
import com.loafer.blog.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String filteredContent;
    private String originalContent;
    private Integer messageType;
    private String filePath;
    private String fileName;
    private Long fileSize;
    private Integer sendStatus;
    private String errorMessage;
    private Integer isTop;
    private LocalDateTime createdAt;
    private User sender;
    private User receiver;

    public MessageVO(Message message) {
        this.id = message.getId();
        this.senderId = message.getSenderId();
        this.receiverId = message.getReceiverId();
        this.content = message.getContent();
        this.originalContent = message.getOriginalContent();
        this.messageType = message.getMessageType();
        this.filePath = message.getFilePath();
        this.fileName = message.getFileName();
        this.fileSize = message.getFileSize();
        this.sendStatus = message.getSendStatus();
        this.errorMessage = message.getErrorMessage();
        this.isTop = message.getIsTop();
        this.createdAt = message.getCreateTime();
        // filteredContent暂时使用content
        this.filteredContent = message.getContent();
    }
}
