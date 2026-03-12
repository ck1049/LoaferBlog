package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.mapper.UserMapper;
import com.loafer.blog.mapper.UserRoleMapper;
import com.loafer.blog.mapper.RoleMapper;
import com.loafer.blog.model.dto.UserDTO;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.model.entity.UserRole;
import com.loafer.blog.model.entity.Role;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.UserVO;
import com.loafer.blog.config.BusinessRSAKeyManager;
import com.loafer.blog.service.UserService;
import com.loafer.blog.utils.FileUploadUtils;
import com.loafer.blog.utils.RSAUtils;
import com.loafer.blog.utils.SensitiveInfoUtils;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    
    // 文件上传路径配置
    @Value("${file.upload.avatar-dir}")
    private String AVATAR_DIR;
    
    @Value("${file.access.prefix}")
    private String ACCESS_PREFIX;

    @Autowired
    private BusinessRSAKeyManager businessRSAKeyManager;
    @Override
    public ResponseVO<UserVO> getCurrentUser(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return ResponseVO.error("用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        // 对邮箱进行脱敏处理
        if (userVO.getEmail() != null && !userVO.getEmail().isEmpty()) {
            try {
                byte[] decryptedEmail = RSAUtils.decrypt(RSAUtils.base64Decode(userVO.getEmail()), businessRSAKeyManager.getPrivateKey());
                userVO.setEmail(SensitiveInfoUtils.maskEmail(new String(decryptedEmail)));
            } catch (Exception e) {
                userVO.setEmail("");
            }
        }
        
        // 添加默认头像功能
        if (userVO.getAvatar() == null || userVO.getAvatar().isEmpty()) {
            userVO.setAvatar(FileUploadUtils.spliceUrl(ACCESS_PREFIX + "/avatars/default-avatar.png"));
        } else {
            userVO.setAvatar(FileUploadUtils.spliceUrl(userVO.getAvatar()));
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
        User user = getById(userId);
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
        if (userDTO.getEmail() != null && !SensitiveInfoUtils.isMaskedEmail(userDTO.getEmail())) {
            try {
                byte[] encryptedEmail = RSAUtils.encrypt(userDTO.getEmail().getBytes(), businessRSAKeyManager.getPublicKey());
                user.setEmail(RSAUtils.base64Encode(encryptedEmail));
            } catch (Exception e) {
                return ResponseVO.error("邮箱加密失败: " + e.getMessage());
            }
        }
        user.setUpdateTime(LocalDateTime.now());

        updateById(user);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        // 对邮箱进行脱敏处理
        if (userVO.getEmail() != null && !userVO.getEmail().isEmpty()) {
            try {
                byte[] decryptedEmail = RSAUtils.decrypt(RSAUtils.base64Decode(userVO.getEmail()), businessRSAKeyManager.getPrivateKey());
                userVO.setEmail(SensitiveInfoUtils.maskEmail(new String(decryptedEmail)));
            } catch (Exception e) {
                userVO.setEmail("邮箱解密失败");
            }
        }
        
        // 添加默认头像功能
        if (userVO.getAvatar() == null || userVO.getAvatar().isEmpty()) {
            userVO.setAvatar(FileUploadUtils.spliceUrl(ACCESS_PREFIX + "/avatars/default-avatar.png"));
        } else {
            userVO.setAvatar(FileUploadUtils.spliceUrl(userVO.getAvatar()));
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
        User user = getById(userId);
        if (user == null) {
            return ResponseVO.error("用户不存在");
        }

        // 检查用户是否为管理员
        QueryWrapper<UserRole> userRoleWrapper = new QueryWrapper<>();
        userRoleWrapper.eq("user_id", userId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleWrapper);
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            if (role != null && "admin".equals(role.getName().toLowerCase())) {
                return ResponseVO.error("管理员账号禁止注销");
            }
        }

        user.setDeleteTime(LocalDateTime.now());
        removeById(user);
        return ResponseVO.success();
    }

    @Override
    public ResponseVO<UserVO> uploadAvatar(Long userId, MultipartFile avatar) {
        User user = getById(userId);
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
            updateById(user);
            
            // 返回更新后的用户信息
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            // 对邮箱进行脱敏处理
            if (userVO.getEmail() != null && !userVO.getEmail().isEmpty()) {
                try {
                    byte[] decryptedEmail = RSAUtils.decrypt(RSAUtils.base64Decode(userVO.getEmail()), businessRSAKeyManager.getPrivateKey());
                    userVO.setEmail(SensitiveInfoUtils.maskEmail(new String(decryptedEmail)));
                } catch (Exception e) {
                    userVO.setEmail("邮箱解密失败");
                }
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
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseVO.error("头像上传失败");
        }
    }
}
