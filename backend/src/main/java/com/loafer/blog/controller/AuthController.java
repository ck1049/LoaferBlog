package com.loafer.blog.controller;

import com.loafer.blog.model.entity.User;
import com.loafer.blog.service.AuthService;
import com.loafer.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        return authService.logout();
    }
}