package com.loafer.blog.model.vo;

import com.loafer.blog.model.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleVO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RoleVO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
        this.createdAt = role.getCreateTime();
        this.updatedAt = role.getUpdateTime();
    }
}
