package com.loafer.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
