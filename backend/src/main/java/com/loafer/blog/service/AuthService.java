package com.loafer.blog.service;

import com.loafer.blog.model.entity.User;

import java.util.Map;

public interface AuthService {
    Map<String, Object> register(User user);
    Map<String, Object> login(User user);
    Map<String, Object> logout();
}