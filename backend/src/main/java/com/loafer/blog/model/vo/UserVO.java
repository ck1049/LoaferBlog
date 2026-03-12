package com.loafer.blog.model.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.loafer.blog.config.BusinessRSAKeyManager;
import com.loafer.blog.utils.RSAUtils;
import com.loafer.blog.utils.SensitiveInfoUtils;
import io.micrometer.common.util.StringUtils;
import lombok.Data;

import java.security.PrivateKey;
import java.util.List;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String bio;
    private String email;
    private List<String> roles;

    public UserVO(com.loafer.blog.model.entity.User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.avatar = user.getAvatar();
        this.bio = user.getBio();
        this.email = user.getEmail();
    }
    
    public UserVO() {
    }

    public String getEmail() {
        if (StringUtils.isNotBlank(this.email)) {
            PrivateKey privateKey = SpringUtil.getBean(BusinessRSAKeyManager.class).getPrivateKey();
            byte[] decryptedEmail = RSAUtils.decrypt(RSAUtils.base64Decode(this.email), privateKey);
            this.email = SensitiveInfoUtils.maskEmail(new String(decryptedEmail));
        }
        return this.email;
    }
}
