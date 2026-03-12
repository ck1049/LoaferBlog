package com.loafer.blog.controller;

import com.loafer.blog.model.dto.PostDTO;
import com.loafer.blog.model.vo.PageResultVO;
import com.loafer.blog.model.vo.PostVO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseVO<PageResultVO<PostVO>> searchPosts(@RequestParam String keyword, 
                                               @RequestParam(required = false, defaultValue = "1") Integer page, 
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        return postService.searchPosts(keyword, page, size);
    }

    @GetMapping("/{id}")
    public ResponseVO<PostVO> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PostMapping
    public ResponseVO<PostVO> createPost(@RequestBody PostDTO postDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return postService.createPost(postDTO, userId);
    }

    @PutMapping("/{id}")
    public ResponseVO<PostVO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return postService.updatePost(id, postDTO, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseVO<Void> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}