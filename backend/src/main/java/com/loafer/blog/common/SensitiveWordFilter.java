package com.loafer.blog.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SensitiveWordFilter {

    private final Map<String, Object> sensitiveWordMap = new HashMap<>();
    private static final String END_FLAG = "end";

    private com.loafer.blog.service.SensitiveWordService sensitiveWordService;

    @Autowired
    public void setSensitiveWordService(com.loafer.blog.service.SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    @PostConstruct
    public void init() {
        reloadWords();
    }

    public void reloadWords(List<String> words) {
        // 清空现有敏感词
        sensitiveWordMap.clear();
        // 添加敏感词
        for (String word : words) {
            addSensitiveWord(word);
        }
    }

    public void reloadWords() {
        // 从数据库加载敏感词
        List<com.loafer.blog.model.entity.SensitiveWord> sensitiveWords = sensitiveWordService.list();
        List<String> words = sensitiveWords.stream()
                .map(com.loafer.blog.model.entity.SensitiveWord::getWord)
                .collect(java.util.stream.Collectors.toList());
        reloadWords(words);
    }

    // 添加敏感词到树结构
    private void addSensitiveWord(String word) {
        Map<String, Object> currentMap = sensitiveWordMap;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            Object obj = currentMap.get(c);
            if (obj == null) {
                Map<String, Object> newMap = new HashMap<>();
                currentMap.put(String.valueOf(c), newMap);
                currentMap = newMap;
            } else {
                currentMap = (Map<String, Object>) obj;
            }
            if (i == word.length() - 1) {
                currentMap.put(END_FLAG, true);
            }
        }
    }

    // 过滤敏感词
    public String filter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        StringBuilder result = new StringBuilder(text);
        for (int i = 0; i < result.length(); i++) {
            int length = checkSensitiveWord(result, i);
            if (length > 0) {
                for (int j = 0; j < length; j++) {
                    result.setCharAt(i + j, '*');
                }
                i += length - 1;
            }
        }
        return result.toString();
    }

    // 检查是否包含敏感词，返回敏感词长度
    private int checkSensitiveWord(StringBuilder text, int startIndex) {
        Map<String, Object> currentMap = sensitiveWordMap;
        int length = 0;
        int maxLength = 0;
        for (int i = startIndex; i < text.length(); i++) {
            String c = String.valueOf(text.charAt(i));
            Object obj = currentMap.get(c);
            if (obj == null) {
                break;
            }
            length++;
            currentMap = (Map<String, Object>) obj;
            if (currentMap.containsKey(END_FLAG)) {
                maxLength = length;
            }
        }
        return maxLength;
    }

    // 检查是否包含敏感词
    public boolean containsSensitiveWord(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            int length = checkSensitiveWord(new StringBuilder(text), i);
            if (length > 0) {
                return true;
            }
        }
        return false;
    }
}