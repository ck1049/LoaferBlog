package com.loafer.blog.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class TokenCache {
    private final Map<String, Long> tokenExpiryMap = new ConcurrentHashMap<>();
    
    /**
     * 添加失效token到缓存
     * @param token token字符串
     * @param expiration 过期时间（秒）
     */
    public void addToken(String token, long expiration) {
        long expiryTime = System.currentTimeMillis() + (expiration * 1000);
        tokenExpiryMap.put(token, expiryTime);
    }
    
    /**
     * 检查token是否在失效缓存中
     * @param token token字符串
     * @return 是否在失效缓存中
     */
    public boolean containsToken(String token) {
        // 清理过期的token
        cleanupExpiredTokens();
        return tokenExpiryMap.containsKey(token);
    }
    
    /**
     * 清理过期的token
     */
    private void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        tokenExpiryMap.entrySet().removeIf(entry -> entry.getValue() < currentTime);
    }
}