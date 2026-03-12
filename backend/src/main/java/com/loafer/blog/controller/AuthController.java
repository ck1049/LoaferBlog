package com.loafer.blog.controller;

import com.loafer.blog.common.RsaUtilsBean;
import com.loafer.blog.dto.LoginDTO;
import com.loafer.blog.dto.RegisterDTO;
import com.loafer.blog.model.dto.ChangePasswordDTO;
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
            String email = null;
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                email = rsaUtils.decrypt(request.getEmail());
            }
            
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
            String password = rsaUtils.decrypt(encryptedPassword);

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(username);
            loginDTO.setPassword(password);
            
            return authService.login(loginDTO);
        } catch (Exception e) {
            log.error("登录失败：{}", e.getMessage());
            return ResponseVO.error("登录失败: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseVO<Void> logout() {
        return authService.logout();
    }


    
    @PutMapping("/password")
    public ResponseVO<Void> changePassword(@RequestAttribute("userId") Long userId, @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            // 解密敏感信息
            String oldPassword = rsaUtils.decrypt(changePasswordDTO.getOldPassword());
            String newPassword = rsaUtils.decrypt(changePasswordDTO.getNewPassword());
            
            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword(oldPassword);
            dto.setNewPassword(newPassword);
            
            return authService.changePassword(userId, dto);
        } catch (Exception e) {
            log.error("密码修改失败\n", e);
            return ResponseVO.error("密码修改失败: " + e.getMessage());
        }
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