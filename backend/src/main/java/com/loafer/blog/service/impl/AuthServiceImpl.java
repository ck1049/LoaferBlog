package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.loafer.blog.dto.LoginDTO;
import com.loafer.blog.dto.RegisterDTO;
import com.loafer.blog.model.dto.ChangePasswordDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    
    // 文件上传路径配置
    @Value("${file.upload.avatar-dir}")
    private String AVATAR_DIR;
    
    @Value("${file.access.prefix}")
    private String ACCESS_PREFIX;
    
    @Value("${file.access.domain}")
    private String ACCESS_DOMAIN;

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

            // 检查用户状态，只有状态为1的用户才能登录
            if (existingUser.getStatus() != 1) {
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
            userVO.setNickname(existingUser.getNickname());
            userVO.setAvatar(existingUser.getAvatar());
            userVO.setBio(existingUser.getBio());
            userVO.setRoles(roles);
            
            // 添加默认头像功能
            if (userVO.getAvatar() == null || userVO.getAvatar().isEmpty()) {
                userVO.setAvatar(ACCESS_DOMAIN + ACCESS_PREFIX + "/avatars/default-avatar.png");
            } else {
                userVO.setAvatar(ACCESS_DOMAIN + userVO.getAvatar());
            }

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
    public ResponseVO<Void> changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseVO.error("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            return ResponseVO.error("旧密码错误");
        }

        // 更新新密码
        String encodedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
        user.setPassword(encodedPassword);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        return ResponseVO.success();
    }
}