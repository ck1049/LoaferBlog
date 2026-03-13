package com.loafer.blog.model.vo;

import lombok.Data;

@Data
public class LoginResponseVO {
    private String token;
    private UserVO user;
}
