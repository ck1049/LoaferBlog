package com.loafer.blog.controller;

import com.loafer.blog.common.RsaUtilsBean;
import com.loafer.blog.model.vo.ResponseVO;
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
    public ResponseVO<Map<String, Object>> getPublicKey() {
        try {
            String publicKey = rsaUtils.getPublicKey();
            Map<String, Object> data = new HashMap<>();
            data.put("publicKey", publicKey);
            return ResponseVO.success(data);
        } catch (Exception e) {
            return ResponseVO.error("获取公钥失败: " + e.getMessage());
        }
    }
}
