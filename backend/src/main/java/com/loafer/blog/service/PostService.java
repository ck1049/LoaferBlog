package com.loafer.blog.service;

import com.loafer.blog.model.dto.PostDTO;
import com.loafer.blog.model.vo.PostVO;
import com.loafer.blog.model.vo.ResponseVO;

import java.util.List;

public interface PostService {
    ResponseVO<List<PostVO>> getPosts();
    ResponseVO<List<PostVO>> searchPosts(String keyword);
    ResponseVO<PostVO> getPost(Long id);
    ResponseVO<PostVO> createPost(PostDTO postDTO);
    ResponseVO<PostVO> updatePost(Long id, PostDTO postDTO);
    ResponseVO<Void> deletePost(Long id);
}