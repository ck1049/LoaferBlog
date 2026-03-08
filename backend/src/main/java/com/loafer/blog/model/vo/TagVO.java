package com.loafer.blog.model.vo;

import com.loafer.blog.model.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagVO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TagVO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.createdAt = tag.getCreateTime();
        this.updatedAt = tag.getUpdateTime();
    }
}
