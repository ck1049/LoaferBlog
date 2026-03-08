package com.loafer.blog.model.vo;

import com.loafer.blog.model.entity.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryVO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryVO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.createdAt = category.getCreateTime();
        this.updatedAt = category.getUpdateTime();
    }
}
