package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.config.FullTextSearchConfig;
import com.loafer.blog.model.dto.PostDTO;
import com.loafer.blog.model.entity.Post;
import com.loafer.blog.model.entity.PostCategory;
import com.loafer.blog.model.entity.PostTag;
import com.loafer.blog.model.vo.CategoryVO;
import com.loafer.blog.model.vo.PageResultVO;
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
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private PostCategoryMapper postCategoryMapper;
    @Autowired
    private PostTagMapper postTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private FullTextSearchConfig fullTextSearchConfig;

    // 高亮处理
    private String highlightKeyword(String text, String keyword) {
        if (text == null || keyword == null || keyword.isEmpty()) {
            return text;
        }
        return text.replaceAll(keyword, "<span class='highLight'>" + keyword + "</span>");
    }

    @Override
    public ResponseVO<List<PostVO>> getPosts() {
        try {
            QueryWrapper<Post> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", 0);
            wrapper.eq("status", 1);
            wrapper.orderByDesc("create_time");
            List<Post> posts = list(wrapper);
            List<PostVO> postVOs = posts.stream().map(this::convertToPostVO).collect(Collectors.toList());
            return ResponseVO.success(postVOs);
        } catch (Exception e) {
            return ResponseVO.error("获取技术贴列表失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<PageResultVO<PostVO>> searchPosts(String keyword, Integer page, Integer size) {
        try {
            Page<Post> pageParam = new Page<>(page, size);
            IPage<Post> postPage;

            // 当关键词为空时，使用方案A
            if (keyword == null || keyword.isEmpty()) {
                QueryWrapper<Post> wrapper = new QueryWrapper<>();
                wrapper.eq("deleted", 0);
                wrapper.eq("status", 1);
                wrapper.orderByDesc("create_time");
                postPage = page(pageParam, wrapper);
            } else {
                // 检测jieba分词器是否可用
                if (fullTextSearchConfig.isJiebaAvailable()) {
                    // 方案B：使用jieba全文检索
                    postPage = baseMapper.searchPostsJieba(pageParam, keyword);
                } else {
                    // 方案A：使用传统like查询
                    postPage = baseMapper.searchPostsLike(pageParam, keyword);
                    
                    // 方案A需要在Java层做高亮处理
                    List<Post> posts = postPage.getRecords();
                    posts.forEach(post -> {
                        post.setTitle(highlightKeyword(post.getTitle(), keyword));
                        post.setContent(highlightKeyword(post.getContent(), keyword));
                    });
                }
            }

            List<PostVO> postVOs = postPage.getRecords().stream()
                    .map(this::convertToPostVO)
                    .collect(Collectors.toList());
            
            PageResultVO<PostVO> pageResult = new PageResultVO<>(
                    postVOs,
                    postPage.getTotal(),
                    postPage.getSize(),
                    postPage.getCurrent()
            );
            
            return ResponseVO.success(pageResult);
        } catch (Exception e) {
            return ResponseVO.error("搜索技术贴失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<PostVO> getPost(Long id) {
        try {
            Post post = getById(id);
            if (post == null) {
                return ResponseVO.error("技术贴不存在");
            }
            if (post.getFavoriteCount() == null) {
                post.setFavoriteCount(0);
            }
            PostVO postVO = convertToPostVO(post);
            return ResponseVO.success(postVO);
        } catch (Exception e) {
            return ResponseVO.error("获取技术贴失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<PostVO> createPost(PostDTO postDTO, Long userId) {
        try {
            String title = XssUtils.filter(postDTO.getTitle());
            String content = XssUtils.filter(postDTO.getContent());
            
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setAuthorId(userId);
            post.setViewCount(0);
            post.setFavoriteCount(0);
            post.setStatus(1);
            post.setCreateTime(LocalDateTime.now());
            post.setUpdateTime(LocalDateTime.now());
            save(post);
            
            if (postDTO.getCategoryIds() != null) {
                for (Long categoryId : postDTO.getCategoryIds()) {
                    PostCategory postCategory = new PostCategory();
                    postCategory.setPostId(post.getId());
                    postCategory.setCategoryId(categoryId);
                    postCategoryMapper.insert(postCategory);
                }
            }
            
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
            Post existingPost = getById(id);
            if (existingPost == null) {
                return ResponseVO.error("技术贴不存在");
            }

            String title = XssUtils.filter(postDTO.getTitle());
            String content = XssUtils.filter(postDTO.getContent());

            existingPost.setTitle(title);
            existingPost.setContent(content);
            existingPost.setUpdateTime(LocalDateTime.now());
            updateById(existingPost);
            
            QueryWrapper<PostCategory> categoryWrapper = new QueryWrapper<>();
            categoryWrapper.eq("post_id", id);
            postCategoryMapper.delete(categoryWrapper);
            
            QueryWrapper<PostTag> tagWrapper = new QueryWrapper<>();
            tagWrapper.eq("post_id", id);
            postTagMapper.delete(tagWrapper);
            
            if (postDTO.getCategoryIds() != null) {
                for (Long categoryId : postDTO.getCategoryIds()) {
                    PostCategory postCategory = new PostCategory();
                    postCategory.setPostId(id);
                    postCategory.setCategoryId(categoryId);
                    postCategoryMapper.insert(postCategory);
                }
            }
            
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
            Post existingPost = getById(id);
            if (existingPost == null) {
                return ResponseVO.error("技术贴不存在");
            }

            QueryWrapper<PostCategory> categoryWrapper = new QueryWrapper<>();
            categoryWrapper.eq("post_id", id);
            postCategoryMapper.delete(categoryWrapper);
            
            QueryWrapper<PostTag> tagWrapper = new QueryWrapper<>();
            tagWrapper.eq("post_id", id);
            postTagMapper.delete(tagWrapper);

            existingPost.setDeleteTime(LocalDateTime.now());
            removeById(existingPost);
            return ResponseVO.success(null);
        } catch (Exception e) {
            return ResponseVO.error("删除技术贴失败: " + e.getMessage());
        }
    }
    
    private PostVO convertToPostVO(Post post) {
        PostVO postVO = new PostVO(post);
        
        QueryWrapper<PostCategory> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.eq("post_id", post.getId());
        List<PostCategory> postCategories = postCategoryMapper.selectList(categoryWrapper);
        if (!postCategories.isEmpty()) {
            List<Long> categoryIds = postCategories.stream().map(PostCategory::getCategoryId).collect(Collectors.toList());
            List<CategoryVO> categoryVOs = categoryMapper.selectBatchIds(categoryIds).stream()
                    .map(CategoryVO::new).collect(Collectors.toList());
            postVO.setCategories(categoryVOs);
        }
        
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
