package com.loafer.blog.service;

import com.loafer.blog.entity.Post;

import java.util.Map;

public interface PostService {
    Map<String, Object> getPosts();
    Map<String, Object> searchPosts(String keyword);
    Map<String, Object> getPost(Long id);
    Map<String, Object> createPost(Post post);
    Map<String, Object> updatePost(Long id, Post post);
    Map<String, Object> deletePost(Long id);
}