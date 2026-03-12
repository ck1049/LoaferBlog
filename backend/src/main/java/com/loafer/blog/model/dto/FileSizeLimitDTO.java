package com.loafer.blog.model.dto;

import lombok.Data;

@Data
public class FileSizeLimitDTO {
    private Long maxSize;
    private String description;
}
