package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.mapper.UserMapper;
import com.loafer.blog.mapper.UserRoleMapper;
import com.loafer.blog.mapper.RoleMapper;
import com.loafer.blog.model.dto.UserDTO;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.model.entity.UserRole;
import com.loafer.blog.model.entity.Role;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.UserVO;
import com.loafer.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    
    // 文件上传路径配置
    @Value("${file.upload.avatar-dir}")
    private String AVATAR_DIR;
    
    @Value("${file.access.prefix}")
    private String ACCESS_PREFIX;

    @Value("${file.access.domain}")
    private String ACCESS_DOMAIN;
    @Override
    public ResponseVO<UserVO> getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseVO.error("用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // 添加默认头像功能
        if (userVO.getAvatar() == null || userVO.getAvatar().isEmpty()) {
            userVO.setAvatar(ACCESS_DOMAIN + ACCESS_PREFIX + "/avatars/default-avatar.png");
        } else {
            userVO.setAvatar(ACCESS_DOMAIN + userVO.getAvatar());
        }
        
        // 添加角色信息的查询和设置
        List<String> roles = new ArrayList<>();
        QueryWrapper<UserRole> userRoleWrapper = new QueryWrapper<>();
        userRoleWrapper.eq("user_id", userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            if (role != null) {
                roles.add(role.getName());
            }
        }
        userVO.setRoles(roles);
        
        return ResponseVO.success(userVO);
    }

    @Override
    public ResponseVO<UserVO> updateCurrentUser(Long userId, UserDTO userDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseVO.error("用户不存在");
        }

        // 更新用户信息
        if (userDTO.getNickname() != null) {
            user.setNickname(userDTO.getNickname());
        }
        if (userDTO.getBio() != null) {
            user.setBio(userDTO.getBio());
        }
        if (userDTO.getAvatar() != null) {
            user.setAvatar(userDTO.getAvatar());
        }
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // 添加默认头像功能
        if (userVO.getAvatar() == null || userVO.getAvatar().isEmpty()) {
            userVO.setAvatar(ACCESS_DOMAIN + ACCESS_PREFIX + "/avatars/default-avatar.png");
        } else {
            userVO.setAvatar(ACCESS_DOMAIN + userVO.getAvatar());
        }
        
        // 添加角色信息的查询和设置
        List<String> roles = new ArrayList<>();
        QueryWrapper<UserRole> userRoleWrapper = new QueryWrapper<>();
        userRoleWrapper.eq("user_id", userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            if (role != null) {
                roles.add(role.getName());
            }
        }
        userVO.setRoles(roles);
        
        return ResponseVO.success(userVO);
    }

    @Override
    public ResponseVO<Void> deleteAccount(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseVO.error("用户不存在");
        }

        // 检查用户是否为管理员
        QueryWrapper<UserRole> userRoleWrapper = new QueryWrapper<>();
        userRoleWrapper.eq("user_id", userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            if (role != null && "ADMIN".equals(role.getName())) {
                return ResponseVO.error("管理员账号禁止注销");
            }
        }

        // 软删除，将状态设置为0
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId)
                .set("status", 0)
                .set("updated_at", LocalDateTime.now());
        userMapper.update(null, updateWrapper);

        return ResponseVO.success();
    }

    @Override
    public ResponseVO<UserVO> uploadAvatar(Long userId, MultipartFile avatar) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseVO.error("用户不存在");
        }

        try {
            // 确保头像目录存在
            File dir = new File(AVATAR_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 生成唯一文件名
            String originalFilename = avatar.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String fileName = UUID.randomUUID() + extension;
            
            // 保存文件到服务器
            File dest = new File(AVATAR_DIR + File.separator + fileName);
            avatar.transferTo(dest);
            
            // 更新用户头像URL
            String avatarUrl = ACCESS_PREFIX + "/avatars/" + fileName;
            user.setAvatar(avatarUrl);
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);
            
            // 返回更新后的用户信息
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            
            // 添加角色信息的查询和设置
            List<String> roles = new ArrayList<>();
            QueryWrapper<UserRole> userRoleWrapper = new QueryWrapper<>();
            userRoleWrapper.eq("user_id", userId);
            List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);
            for (UserRole userRole : userRoles) {
                Role role = roleMapper.selectById(userRole.getRoleId());
                if (role != null) {
                    roles.add(role.getName());
                }
            }
            userVO.setRoles(roles);
            
            return ResponseVO.success(userVO);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseVO.error("头像上传失败");
        }
    }
}
