package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.entity.SensitiveWord;

import java.util.List;

public interface SensitiveWordService extends IService<SensitiveWord> {
    List<SensitiveWord> getAllSensitiveWords();
    SensitiveWord createSensitiveWord(SensitiveWord sensitiveWord);
    boolean deleteSensitiveWord(Long id);
    void reloadSensitiveWords();
}
