package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.entity.Category;
import com.loafer.blog.entity.PostCategory;
import com.loafer.blog.mapper.CategoryMapper;
import com.loafer.blog.mapper.PostCategoryMapper;
import com.loafer.blog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final PostCategoryMapper postCategoryMapper;

    public CategoryServiceImpl(PostCategoryMapper postCategoryMapper) {
        this.postCategoryMapper = postCategoryMapper;
    }

    @Override
    public List<Category> getAllCategories() {
        return baseMapper.selectList(null);
    }

    @Override
    public Category createCategory(Category category) {
        baseMapper.insert(category);
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        baseMapper.updateById(category);
        return category;
    }

    @Override
    public boolean deleteCategory(Long id) {
        // 先删除分类与帖子的关联
        QueryWrapper<PostCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", id);
        postCategoryMapper.delete(wrapper);
        // 再删除分类
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public List<Category> getCategoriesByPostId(Long postId) {
        // 通过PostCategory关联查询分类
        QueryWrapper<PostCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId);
        List<PostCategory> postCategories = postCategoryMapper.selectList(wrapper);
        
        List<Long> categoryIds = postCategories.stream()
                .map(PostCategory::getCategoryId)
                .toList();
        
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        
        return baseMapper.selectBatchIds(categoryIds);
    }

    @Override
    public void addCategoryToPost(Long postId, List<Long> categoryIds) {
        // 先删除现有关联
        QueryWrapper<PostCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId);
        postCategoryMapper.delete(wrapper);
        
        // 添加新关联
        for (Long categoryId : categoryIds) {
            PostCategory postCategory = new PostCategory();
            postCategory.setPostId(postId);
            postCategory.setCategoryId(categoryId);
            postCategoryMapper.insert(postCategory);
        }
    }
}