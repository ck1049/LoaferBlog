package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.model.dto.PostDTO;
import com.loafer.blog.model.entity.Post;
import com.loafer.blog.model.entity.PostCategory;
import com.loafer.blog.model.entity.PostTag;
import com.loafer.blog.model.vo.CategoryVO;
import com.loafer.blog.model.vo.PostVO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.TagVO;
import com.loafer.blog.mapper.CategoryMapper;
import com.loafer.blog.mapper.PostCategoryMapper;
import com.loafer.blog.mapper.PostMapper;
import com.loafer.blog.mapper.PostTagMapper;
import com.loafer.blog.mapper.TagMapper;
import com.loafer.blog.service.PostService;
import com.loafer.blog.utils.XssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostCategoryMapper postCategoryMapper;
    @Autowired
    private PostTagMapper postTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseVO<List<PostVO>> getPosts() {
        try {
            // 按发布时间倒序排列
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("create_time");
            List<Post> posts = postMapper.selectList(wrapper);
            List<PostVO> postVOs = posts.stream().map(this::convertToPostVO).collect(Collectors.toList());
            return ResponseVO.success(postVOs);
        } catch (Exception e) {
            return ResponseVO.error("获取技术贴列表失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<List<PostVO>> searchPosts(String keyword) {
        try {
            // 使用PostgreSQL的全文搜索
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.like("title", keyword).or().like("content", keyword);
            wrapper.orderByDesc("create_time");
            List<Post> posts = postMapper.selectList(wrapper);
            List<PostVO> postVOs = posts.stream().map(this::convertToPostVO).collect(Collectors.toList());
            return ResponseVO.success(postVOs);
        } catch (Exception e) {
            return ResponseVO.error("搜索技术贴失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<PostVO> getPost(Long id) {
        try {
            Post post = postMapper.selectById(id);
            if (post == null) {
                return ResponseVO.error("技术贴不存在");
            }
            // 增加阅读量
            post.setViewCount(post.getViewCount() + 1);
            postMapper.updateById(post);
            PostVO postVO = convertToPostVO(post);
            return ResponseVO.success(postVO);
        } catch (Exception e) {
            return ResponseVO.error("获取技术贴失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<PostVO> createPost(PostDTO postDTO, Long userId) {
        try {
            // 防XSS处理
            String title = XssUtils.filter(postDTO.getTitle());
            String content = XssUtils.filter(postDTO.getContent());
            
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setAuthorId(userId);
            post.setViewCount(0);
            post.setStatus(1);
            post.setCreateTime(LocalDateTime.now());
            post.setUpdateTime(LocalDateTime.now());
            postMapper.insert(post);
            
            // 处理分类关联
            if (postDTO.getCategoryIds() != null) {
                for (Long categoryId : postDTO.getCategoryIds()) {
                    PostCategory postCategory = new PostCategory();
                    postCategory.setPostId(post.getId());
                    postCategory.setCategoryId(categoryId);
                    postCategoryMapper.insert(postCategory);
                }
            }
            
            // 处理标签关联
            if (postDTO.getTagIds() != null) {
                for (Long tagId : postDTO.getTagIds()) {
                    PostTag postTag = new PostTag();
                    postTag.setPostId(post.getId());
                    postTag.setTagId(tagId);
                    postTagMapper.insert(postTag);
                }
            }
            
            PostVO postVO = convertToPostVO(post);
            return ResponseVO.success(postVO);
        } catch (Exception e) {
            return ResponseVO.error("创建技术贴失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<PostVO> updatePost(Long id, PostDTO postDTO, Long userId) {
        try {
            Post existingPost = postMapper.selectById(id);
            if (existingPost == null) {
                return ResponseVO.error("技术贴不存在");
            }

            // 防XSS处理
            String title = XssUtils.filter(postDTO.getTitle());
            String content = XssUtils.filter(postDTO.getContent());

            existingPost.setTitle(title);
            existingPost.setContent(content);
            existingPost.setUpdateTime(LocalDateTime.now());
            postMapper.updateById(existingPost);
            
            // 先删除现有关联
            QueryWrapper<PostCategory> categoryWrapper = new QueryWrapper<>();
            categoryWrapper.eq("post_id", id);
            postCategoryMapper.delete(categoryWrapper);
            
            QueryWrapper<PostTag> tagWrapper = new QueryWrapper<>();
            tagWrapper.eq("post_id", id);
            postTagMapper.delete(tagWrapper);
            
            // 处理分类关联
            if (postDTO.getCategoryIds() != null) {
                for (Long categoryId : postDTO.getCategoryIds()) {
                    PostCategory postCategory = new PostCategory();
                    postCategory.setPostId(id);
                    postCategory.setCategoryId(categoryId);
                    postCategoryMapper.insert(postCategory);
                }
            }
            
            // 处理标签关联
            if (postDTO.getTagIds() != null) {
                for (Long tagId : postDTO.getTagIds()) {
                    PostTag postTag = new PostTag();
                    postTag.setPostId(id);
                    postTag.setTagId(tagId);
                    postTagMapper.insert(postTag);
                }
            }
            
            PostVO postVO = convertToPostVO(existingPost);
            return ResponseVO.success(postVO);
        } catch (Exception e) {
            return ResponseVO.error("更新技术贴失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<Void> deletePost(Long id) {
        try {
            Post existingPost = postMapper.selectById(id);
            if (existingPost == null) {
                return ResponseVO.error("技术贴不存在");
            }

            // 先删除关联
            QueryWrapper<PostCategory> categoryWrapper = new QueryWrapper<>();
            categoryWrapper.eq("post_id", id);
            postCategoryMapper.delete(categoryWrapper);
            
            QueryWrapper<PostTag> tagWrapper = new QueryWrapper<>();
            tagWrapper.eq("post_id", id);
            postTagMapper.delete(tagWrapper);
            
            postMapper.deleteById(id);
            return ResponseVO.success(null);
        } catch (Exception e) {
            return ResponseVO.error("删除技术贴失败: " + e.getMessage());
        }
    }
    
    private PostVO convertToPostVO(Post post) {
        PostVO postVO = new PostVO(post);
        
        // 获取分类
        QueryWrapper<PostCategory> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.eq("post_id", post.getId());
        List<PostCategory> postCategories = postCategoryMapper.selectList(categoryWrapper);
        if (!postCategories.isEmpty()) {
            List<Long> categoryIds = postCategories.stream().map(PostCategory::getCategoryId).collect(Collectors.toList());
            List<CategoryVO> categoryVOs = categoryMapper.selectBatchIds(categoryIds).stream()
                    .map(CategoryVO::new).collect(Collectors.toList());
            postVO.setCategories(categoryVOs);
        }
        
        // 获取标签
        QueryWrapper<PostTag> tagWrapper = new QueryWrapper<>();
        tagWrapper.eq("post_id", post.getId());
        List<PostTag> postTags = postTagMapper.selectList(tagWrapper);
        if (!postTags.isEmpty()) {
            List<Long> tagIds = postTags.stream().map(PostTag::getTagId).collect(Collectors.toList());
            List<TagVO> tagVOs = tagMapper.selectBatchIds(tagIds).stream()
                    .map(TagVO::new).collect(Collectors.toList());
            postVO.setTags(tagVOs);
        }
        
        return postVO;
    }
}