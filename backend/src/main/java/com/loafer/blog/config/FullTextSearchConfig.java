package com.loafer.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
public class FullTextSearchConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Boolean jiebaAvailable = null;

    @PostConstruct
    public void init() {
        checkJiebaAvailable();
    }

    public boolean isJiebaAvailable() {
        if (jiebaAvailable == null) {
            return checkJiebaAvailable();
        }
        return jiebaAvailable;
    }

    private boolean checkJiebaAvailable() {
        try {
            // 尝试执行CREATE EXTENSION IF NOT EXISTS pg_jieba
            jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS pg_jieba");
            // 尝试使用jiebacfg分词器
            jdbcTemplate.queryForObject("SELECT to_tsvector('jiebacfg', '测试')", String.class);
            jiebaAvailable = true;
        } catch (Exception e) {
            jiebaAvailable = false;
        }
        return jiebaAvailable;
    }
}
