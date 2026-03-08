package com.loafer.blog.controller;

import com.loafer.blog.model.dto.PostDTO;
import com.loafer.blog.model.vo.PostVO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseVO<List<PostVO>> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/search")
    public ResponseVO<List<PostVO>> searchPosts(@RequestParam String keyword) {
        return postService.searchPosts(keyword);
    }

    @GetMapping("/{id}")
    public ResponseVO<PostVO> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PostMapping
    public ResponseVO<PostVO> createPost(@RequestBody PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PutMapping("/{id}")
    public ResponseVO<PostVO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        return postService.updatePost(id, postDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseVO<Void> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}