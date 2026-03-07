package com.loafer.blog.controller;

import com.loafer.blog.entity.Post;
import com.loafer.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public Map<String, Object> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/search")
    public Map<String, Object> searchPosts(@RequestParam String keyword) {
        return postService.searchPosts(keyword);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PostMapping
    public Map<String, Object> createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updatePost(@PathVariable Long id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}