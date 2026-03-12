package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.dto.PostDTO;
import com.loafer.blog.model.entity.Post;
import com.loafer.blog.model.vo.PageResultVO;
import com.loafer.blog.model.vo.PostVO;
import com.loafer.blog.model.vo.ResponseVO;

import java.util.List;

public interface PostService extends IService<Post> {
    ResponseVO<List<PostVO>> getPosts();
    ResponseVO<PageResultVO<PostVO>> searchPosts(String keyword, Integer page, Integer size);
    ResponseVO<PostVO> getPost(Long id);
    ResponseVO<PostVO> createPost(PostDTO postDTO, Long userId);
    ResponseVO<PostVO> updatePost(Long id, PostDTO postDTO, Long userId);
    ResponseVO<Void> deletePost(Long id);
}