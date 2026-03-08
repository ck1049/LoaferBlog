package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.dto.LoginDTO;
import com.loafer.blog.dto.RegisterDTO;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.model.entity.UserRole;
import com.loafer.blog.model.entity.Role;
import com.loafer.blog.mapper.UserMapper;
import com.loafer.blog.mapper.UserRoleMapper;
import com.loafer.blog.mapper.RoleMapper;
import com.loafer.blog.service.AuthService;
import com.loafer.blog.utils.JwtUtils;
import com.loafer.blog.vo.LoginResponseVO;
import com.loafer.blog.vo.ResponseVO;
import com.loafer.blog.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public ResponseVO<Void> register(RegisterDTO registerDTO) {
        try {
            // 检查用户名是否已存在
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", registerDTO.getUsername());
            if (userMapper.selectOne(wrapper) != null) {
                return ResponseVO.error(400, "用户名已存在");
            }

            // 创建用户
            User user = new User();
            user.setUsername(registerDTO.getUsername());
            user.setEmail(registerDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            user.setNickname(registerDTO.getNickname());
            user.setStatus(1);
            userMapper.insert(user);

            // 为新用户分配默认角色（普通用户）
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(2L); // 普通用户角色ID
            userRoleMapper.insert(userRole);

            return ResponseVO.success(null);
        } catch (Exception e) {
            return ResponseVO.error("注册失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<LoginResponseVO> login(LoginDTO loginDTO) {
        try {
            // 查找用户
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", loginDTO.getUsername());
            User existingUser = userMapper.selectOne(wrapper);

            if (existingUser == null || !passwordEncoder.matches(loginDTO.getPassword(), existingUser.getPassword())) {
                return ResponseVO.error(400, "用户名或密码错误");
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

            // 构建用户信息
            UserVO userVO = new UserVO();
            userVO.setId(existingUser.getId());
            userVO.setUsername(existingUser.getUsername());
            userVO.setEmail(existingUser.getEmail());
            userVO.setRoles(roles);

            // 构建登录响应
            LoginResponseVO loginResponseVO = new LoginResponseVO();
            loginResponseVO.setToken(token);
            loginResponseVO.setUser(userVO);

            return ResponseVO.success("登录成功", loginResponseVO);
        } catch (Exception e) {
            return ResponseVO.error("登录失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<Void> logout() {
        return ResponseVO.success(null);
    }

    @Override
    public ResponseVO<UserVO> getCurrentUser(Long userId) {
        try {
            // 查找用户
            User existingUser = userMapper.selectById(userId);
            if (existingUser == null) {
                return ResponseVO.error(404, "用户不存在");
            }

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

            // 构建用户信息
            UserVO userVO = new UserVO();
            userVO.setId(existingUser.getId());
            userVO.setUsername(existingUser.getUsername());
            userVO.setEmail(existingUser.getEmail());
            userVO.setRoles(roles);

            return ResponseVO.success("获取用户信息成功", userVO);
        } catch (Exception e) {
            return ResponseVO.error("获取用户信息失败: " + e.getMessage());
        }
    }
}