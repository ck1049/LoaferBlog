package com.loafer.blog.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagDTO {
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称长度不能超过50个字符")
    private String name;
    
    @Size(max = 255, message = "标签描述长度不能超过255个字符")
    private String description;
    
    private Integer status = 1;
}