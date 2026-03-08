package com.loafer.blog.model.vo;

import com.loafer.blog.model.entity.Announcement;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnouncementVO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean read;

    public AnnouncementVO(Announcement announcement) {
        this.id = announcement.getId();
        this.title = announcement.getTitle();
        this.content = announcement.getContent();
        this.createdAt = announcement.getCreateTime();
        this.updatedAt = announcement.getUpdateTime();
        this.read = false;
    }
}
