package com.loafer.blog.controller;

import com.loafer.blog.common.RsaUtilsBean;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RsaUtilsBean rsaUtils;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        try {
            // 解密敏感信息
            String username = request.get("username");
            String encryptedPassword = request.get("password");
            String encryptedEmail = request.get("email");
            
            String password = rsaUtils.decrypt(encryptedPassword);
            String email = rsaUtils.decrypt(encryptedEmail);
            
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setNickname(request.get("nickname"));
            
            return authService.register(user);
        } catch (Exception e) {
            log.error("注册失败\n", e);
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("code", 500);
            result.put("message", "注册失败: " + e.getMessage());
            return result;
        }
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        try {
            // 解密敏感信息
            String username = request.get("username");
            String encryptedPassword = request.get("password");
            
            String password = rsaUtils.decrypt(encryptedPassword);
            
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            
            return authService.login(user);
        } catch (Exception e) {
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("code", 500);
            result.put("message", "登录失败: " + e.getMessage());
            return result;
        }
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        return authService.logout();
    }

    // 测试RSA加密解密功能
    @GetMapping("/test-rsa")
    public Map<String, Object> testRsa() {
        try {
            // 测试数据
            String password = "password123";
            String email = "test@example.com";
            
            // 获取公钥
            String publicKey = rsaUtils.getPublicKey();
            
            // 使用公钥加密
            String encryptedPassword = rsaUtils.encrypt(password, publicKey);
            String encryptedEmail = rsaUtils.encrypt(email, publicKey);
            
            // 使用私钥解密
            String decryptedPassword = rsaUtils.decrypt(encryptedPassword);
            String decryptedEmail = rsaUtils.decrypt(encryptedEmail);
            
            // 验证解密结果
            boolean success = password.equals(decryptedPassword) && email.equals(decryptedEmail);
            
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("code", success ? 200 : 500);
            result.put("message", success ? "RSA加密解密测试成功" : "RSA加密解密测试失败");
            result.put("originalPassword", password);
            result.put("decryptedPassword", decryptedPassword);
            result.put("originalEmail", email);
            result.put("decryptedEmail", decryptedEmail);
            
            return result;
        } catch (Exception e) {
            log.error("RSA测试失败: {}", e.getMessage());
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("code", 500);
            result.put("message", "RSA测试失败: " + e.getMessage());
            return result;
        }
    }
}