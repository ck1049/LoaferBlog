package com.loafer.blog.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDTO {
    private String title;
    private String content;
    private List<Long> categoryIds;
    private List<Long> tagIds;
}
