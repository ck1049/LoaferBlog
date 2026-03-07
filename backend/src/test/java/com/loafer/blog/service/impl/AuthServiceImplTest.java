package com.loafer.blog.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
public class AuthServiceImplTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPasswordEncoding() {
        // 测试密码加密功能
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // 验证密码是否被加密
        assertTrue(!rawPassword.equals(encodedPassword), "密码应该被加密");
        
        // 验证密码是否可以被正确验证
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "密码验证应该通过");
        
        // 验证不同的密码应该返回不同的加密结果
        String rawPassword2 = "password456";
        String encodedPassword2 = passwordEncoder.encode(rawPassword2);
        assertTrue(!encodedPassword.equals(encodedPassword2), "不同密码的加密结果应该不同");
    }

    @Test
    public void testPasswordMatching() {

        String defaultPassword = "admin123";
        log.info("{} encoded to [{}]", defaultPassword, passwordEncoder.encode(defaultPassword));
        defaultPassword = "user123";
        log.info("{} encoded to [{}]", defaultPassword, passwordEncoder.encode(defaultPassword));

        // 测试密码匹配功能
        String rawPassword = "testPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // 正确的密码应该匹配成功
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "正确的密码应该匹配成功");
        
        // 错误的密码应该匹配失败
        String wrongPassword = "wrongPassword";
        assertTrue(!passwordEncoder.matches(wrongPassword, encodedPassword), "错误的密码应该匹配失败");
    }
}
