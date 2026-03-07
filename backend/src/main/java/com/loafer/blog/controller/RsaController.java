package com.loafer.blog.controller;

import com.loafer.blog.common.RsaUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rsa")
public class RsaController {

    @Autowired
    private RsaUtilsBean rsaUtils;

    @GetMapping("/public-key")
    public Map<String, Object> getPublicKey() {
        Map<String, Object> result = new HashMap<>();
        try {
            String publicKey = rsaUtils.getPublicKey();
            result.put("code", 200);
            result.put("message", "获取公钥成功");
            result.put("publicKey", publicKey);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取公钥失败: " + e.getMessage());
        }
        return result;
    }
}
