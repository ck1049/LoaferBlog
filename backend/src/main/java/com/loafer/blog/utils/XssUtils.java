package com.loafer.blog.utils;

import org.springframework.stereotype.Component;

/**
 * 防XSS攻击工具类
 */
@Component
public class XssUtils {

    /**
     * 过滤HTML标签和恶意脚本
     * @param input 输入字符串
     * @return 过滤后的字符串
     */
    public static String filter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 过滤HTML标签
        input = input.replaceAll("<script[^>]*>.*?</script>", "");
        input = input.replaceAll("<iframe[^>]*>.*?</iframe>", "");
        input = input.replaceAll("<object[^>]*>.*?</object>", "");
        input = input.replaceAll("<embed[^>]*>.*?</embed>", "");
        input = input.replaceAll("<link[^>]*>.*?</link>", "");
        input = input.replaceAll("<style[^>]*>.*?</style>", "");

        // 过滤JavaScript事件
        input = input.replaceAll("on\\w+\\s*=\\s*[\"'].*?[\"']", "");
        input = input.replaceAll("on\\w+\\s*=\\s*[^\\s]+", "");

        // 过滤危险的协议
        input = input.replaceAll("javascript:\\s*", "");
        input = input.replaceAll("data:\\s*", "");
        input = input.replaceAll("vbscript:\\s*", "");

        // 过滤特殊字符
        input = input.replaceAll("<", "&lt;");
        input = input.replaceAll(">", "&gt;");
        input = input.replaceAll("'", "&#39;");
        input = input.replaceAll("\"", "&quot;");
        input = input.replaceAll("&", "&amp;");

        return input;
    }

    /**
     * 检查是否包含XSS攻击代码
     * @param input 输入字符串
     * @return 是否包含XSS攻击代码
     */
    public static boolean containsXss(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // 检查脚本标签
        if (input.matches(".*<script[^>]*>.*?</script>.*")) {
            return true;
        }

        // 检查JavaScript事件
        if (input.matches(".*on\\w+\\s*=\\s*[\"'].*?[\"'].*")) {
            return true;
        }

        // 检查危险协议
        if (input.matches(".*javascript:.*")) {
            return true;
        }

        return false;
    }
}
