package com.loafer.blog.utils;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SensitiveWordFilter {
    private Set<String> sensitiveWords = new HashSet<>();

    public void addSensitiveWord(String word) {
        sensitiveWords.add(word);
    }

    public void addSensitiveWords(Set<String> words) {
        sensitiveWords.addAll(words);
    }

    public String filter(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        String filteredContent = content;
        for (String word : sensitiveWords) {
            if (filteredContent.contains(word)) {
                String replacement = "*" .repeat(word.length());
                filteredContent = filteredContent.replace(word, replacement);
            }
        }
        return filteredContent;
    }

    public Set<String> getSensitiveWords() {
        return sensitiveWords;
    }

    public void reloadWords(List<String> words) {
        sensitiveWords.clear();
        sensitiveWords.addAll(words);
    }
}