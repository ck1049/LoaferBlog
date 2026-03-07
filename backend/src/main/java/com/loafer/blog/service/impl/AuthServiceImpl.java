package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.model.entity.UserRole;
import com.loafer.blog.model.entity.Role;
import com.loafer.blog.mapper.UserMapper;
import com.loafer.blog.mapper.UserRoleMapper;
import com.loafer.blog.mapper.RoleMapper;
import com.loafer.blog.service.AuthService;
import com.loafer.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
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
            user.setStatus(1);
            userMapper.insert(user);

            // 为新用户分配默认角色（普通用户）
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(2L); // 普通用户角色ID
            userRoleMapper.insert(userRole);

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

            // 获取用户角色
            List<String> roles = new ArrayList<>();
            QueryWrapper<UserRole> userRoleWrapper = new QueryWrapper<>();
            userRoleWrapper.eq("user_id", existingUser.getId());
            List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);
            for (UserRole userRole : userRoles) {
                Role role = roleMapper.selectById(userRole.getRoleId());
                if (role != null) {
                    roles.add(role.getName());
                }
            }

            // 构建返回的用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", existingUser.getId());
            userInfo.put("username", existingUser.getUsername());
            userInfo.put("email", existingUser.getEmail());
            userInfo.put("roles", roles);

            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("token", token);
            result.put("user", userInfo);
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