package com.loafer.blog.utils;

public class SensitiveInfoUtils {
    /**
     * 邮箱脱敏处理
     * 保留前两位和域名，中间用****替换
     * @param email 邮箱地址
     * @return 脱敏后的邮箱地址
     */
    public static String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            // 邮箱前缀长度小于等于2，只保留第一位
            return email.charAt(0) + "****" + email.substring(atIndex);
        }
        
        return email.substring(0, 2) + "****" + email.substring(atIndex);
    }
    
    /**
     * 检查邮箱是否为脱敏格式
     * @param email 邮箱地址
     * @return 是否为脱敏格式
     */
    public static boolean isMaskedEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        return email.contains("****");
    }
}