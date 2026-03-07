package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.entity.Post;
import com.loafer.blog.mapper.PostMapper;
import com.loafer.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;

    @Override
    public Map<String, Object> getPosts() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 按发布时间倒序排列
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("create_time");
            List<Post> posts = postMapper.selectList(wrapper);
            result.put("code", 200);
            result.put("message", "获取技术贴列表成功");
            result.put("data", posts);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取技术贴列表失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> searchPosts(String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 使用PostgreSQL的全文搜索
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.like("title", keyword).or().like("content", keyword);
            wrapper.orderByDesc("create_time");
            List<Post> posts = postMapper.selectList(wrapper);
            result.put("code", 200);
            result.put("message", "搜索技术贴成功");
            result.put("data", posts);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "搜索技术贴失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getPost(Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Post post = postMapper.selectById(id);
            if (post == null) {
                result.put("code", 400);
                result.put("message", "技术贴不存在");
                return result;
            }
            // 增加阅读量
            post.setViewCount(post.getViewCount() + 1);
            postMapper.updateById(post);
            result.put("code", 200);
            result.put("message", "获取技术贴成功");
            result.put("data", post);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取技术贴失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> createPost(Post post) {
        Map<String, Object> result = new HashMap<>();
        try {
            post.setViewCount(0);
            post.setStatus(true);
            postMapper.insert(post);
            result.put("code", 200);
            result.put("message", "创建技术贴成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "创建技术贴失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> updatePost(Long id, Post post) {
        Map<String, Object> result = new HashMap<>();
        try {
            Post existingPost = postMapper.selectById(id);
            if (existingPost == null) {
                result.put("code", 400);
                result.put("message", "技术贴不存在");
                return result;
            }

            post.setId(id);
            postMapper.updateById(post);
            result.put("code", 200);
            result.put("message", "更新技术贴成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新技术贴失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> deletePost(Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Post existingPost = postMapper.selectById(id);
            if (existingPost == null) {
                result.put("code", 400);
                result.put("message", "技术贴不存在");
                return result;
            }

            postMapper.deleteById(id);
            result.put("code", 200);
            result.put("message", "删除技术贴成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除技术贴失败: " + e.getMessage());
        }
        return result;
    }
}