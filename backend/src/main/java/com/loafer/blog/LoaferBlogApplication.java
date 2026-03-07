package com.loafer.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.loafer.blog.mapper")
public class LoaferBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoaferBlogApplication.class, args);
    }
}