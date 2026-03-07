package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.entity.SensitiveWord;
import com.loafer.blog.mapper.SensitiveWordMapper;
import com.loafer.blog.service.SensitiveWordService;
import com.loafer.blog.utils.SensitiveWordFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {

    private final SensitiveWordFilter sensitiveWordFilter;

    public SensitiveWordServiceImpl(SensitiveWordFilter sensitiveWordFilter) {
        this.sensitiveWordFilter = sensitiveWordFilter;
    }

    @Override
    public List<SensitiveWord> getAllSensitiveWords() {
        return baseMapper.selectList(null);
    }

    @Override
    public SensitiveWord createSensitiveWord(SensitiveWord sensitiveWord) {
        baseMapper.insert(sensitiveWord);
        reloadSensitiveWords();
        return sensitiveWord;
    }

    @Override
    public boolean deleteSensitiveWord(Long id) {
        boolean deleted = baseMapper.deleteById(id) > 0;
        if (deleted) {
            reloadSensitiveWords();
        }
        return deleted;
    }

    @Override
    public void reloadSensitiveWords() {
        List<SensitiveWord> sensitiveWords = baseMapper.selectList(null);
        List<String> words = sensitiveWords.stream()
                .map(SensitiveWord::getWord)
                .collect(Collectors.toList());
        sensitiveWordFilter.reloadWords(words);
    }
}
