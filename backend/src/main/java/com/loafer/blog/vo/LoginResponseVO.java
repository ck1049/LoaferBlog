package com.loafer.blog.vo;

import lombok.Data;

@Data
public class LoginResponseVO {
    private String token;
    private UserVO user;
}
