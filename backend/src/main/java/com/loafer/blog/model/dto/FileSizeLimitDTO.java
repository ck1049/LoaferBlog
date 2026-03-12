package com.loafer.blog.model.dto;

import lombok.Data;

@Data
public class FileSizeLimitDTO {
    private Long imageMaxSize;
    private Long videoMaxSize;
    private Long otherMaxSize;
    private String imageDescription;
    private String videoDescription;
    private String otherDescription;
}
