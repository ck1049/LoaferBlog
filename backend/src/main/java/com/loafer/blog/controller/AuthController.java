package com.loafer.blog.controller;

import com.loafer.blog.common.RsaUtilsBean;
import com.loafer.blog.dto.LoginDTO;
import com.loafer.blog.dto.RegisterDTO;
import com.loafer.blog.service.AuthService;
import com.loafer.blog.vo.LoginResponseVO;
import com.loafer.blog.vo.ResponseVO;
import com.loafer.blog.vo.UserVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RsaUtilsBean rsaUtils;

    @PostMapping("/register")
    public ResponseVO<Void> register(@Valid @RequestBody RegisterDTO request) {
        try {
            // 解密敏感信息
            String password = rsaUtils.decrypt(request.getPassword());
            String email = rsaUtils.decrypt(request.getEmail());
            
            RegisterDTO registerDTO = new RegisterDTO();
            registerDTO.setUsername(request.getUsername());
            registerDTO.setPassword(password);
            registerDTO.setEmail(email);
            registerDTO.setNickname(request.getNickname());
            
            return authService.register(registerDTO);
        } catch (Exception e) {
            log.error("注册失败\n", e);
            return ResponseVO.error("注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseVO<LoginResponseVO> login(@Valid @RequestBody LoginDTO request) {
        try {
            // 解密敏感信息
            String username = request.getUsername();
            String encryptedPassword = request.getPassword();
            
            System.out.println("登录请求，用户名：" + username);
            System.out.println("加密密码长度：" + encryptedPassword.length());
            
            String password = rsaUtils.decrypt(encryptedPassword);
            System.out.println("解密后密码：" + password);
            
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(username);
            loginDTO.setPassword(password);
            
            return authService.login(loginDTO);
        } catch (Exception e) {
            System.out.println("登录失败：" + e.getMessage());
            e.printStackTrace();
            return ResponseVO.error("登录失败: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseVO<Void> logout() {
        return authService.logout();
    }

    @GetMapping("/me")
    public ResponseVO<UserVO> getCurrentUser(@RequestAttribute("userId") Long userId) {
        return authService.getCurrentUser(userId);
    }

    // 测试RSA加密解密功能
    @GetMapping("/test-rsa")
    public ResponseVO<java.util.Map<String, Object>> testRsa() {
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
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("originalPassword", password);
            result.put("decryptedPassword", decryptedPassword);
            result.put("originalEmail", email);
            result.put("decryptedEmail", decryptedEmail);
            
            if (success) {
                return ResponseVO.success(result);
            } else {
                return ResponseVO.error("RSA加密解密测试失败");
            }
        } catch (Exception e) {
            log.error("RSA测试失败: {}", e.getMessage());
            return ResponseVO.error("RSA测试失败: " + e.getMessage());
        }
    }
}