package com.loafer.blog.config;

import com.loafer.blog.mapper.UserMapper;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.utils.AvatarGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ApplicationInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserMapper userMapper;

    @Value("${file.upload.avatar-dir}")
    private String AVATAR_DIR;

    @Value("${file.access.prefix}")
    private String ACCESS_PREFIX;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 为没有头像的用户生成默认头像
        generateDefaultAvatars();
    }

    private void generateDefaultAvatars() {
        try {
            // 查询所有头像为空的用户
            List<User> users = userMapper.selectList(null);
            for (User user : users) {
                if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
                    // 生成默认头像
                    String fileName = AvatarGenerator.generateAvatar(user.getUsername(), AVATAR_DIR);
                    // 生成访问 URL
                    String avatarUrl = ACCESS_PREFIX + "/avatars/" + fileName;
                    // 更新用户头像 URL
                    user.setAvatar(avatarUrl);
                    userMapper.updateById(user);
                    log.info("为用户 {} 生成默认头像: {}", user.getUsername(), avatarUrl);
                }
            }
        } catch (IOException e) {
            log.error("生成默认头像失败: {}", e.getMessage());
        }
    }
}
