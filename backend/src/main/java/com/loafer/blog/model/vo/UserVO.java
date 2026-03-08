package com.loafer.blog.model.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String bio;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status;
    private List<String> roles;
}
