package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.entity.User;
import com.loafer.blog.mapper.UserMapper;
import com.loafer.blog.service.AuthService;
import com.loafer.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查用户名是否已存在
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", user.getUsername());
            if (userMapper.selectOne(wrapper) != null) {
                result.put("code", 400);
                result.put("message", "用户名已存在");
                return result;
            }

            // 加密密码
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(true);
            userMapper.insert(user);

            result.put("code", 200);
            result.put("message", "注册成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "注册失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> login(User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 查找用户
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", user.getUsername());
            User existingUser = userMapper.selectOne(wrapper);

            if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                result.put("code", 400);
                result.put("message", "用户名或密码错误");
                return result;
            }

            // 生成JWT token
            String token = jwtUtils.generateToken(existingUser.getId());

            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("token", token);
            result.put("user", existingUser);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "注销成功");
        return result;
    }
}