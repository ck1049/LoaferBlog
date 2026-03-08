package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.model.dto.CategoryDTO;
import com.loafer.blog.model.entity.Category;
import com.loafer.blog.model.entity.PostCategory;
import com.loafer.blog.model.vo.CategoryVO;
import com.loafer.blog.mapper.CategoryMapper;
import com.loafer.blog.mapper.PostCategoryMapper;
import com.loafer.blog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final PostCategoryMapper postCategoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper, PostCategoryMapper postCategoryMapper) {
        this.categoryMapper = categoryMapper;
        this.postCategoryMapper = postCategoryMapper;
    }

    @Override
    public List<CategoryVO> getAllCategories() {
        List<Category> categories = categoryMapper.selectList(null);
        return categories.stream()
                .map(CategoryVO::new)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryVO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setStatus(categoryDTO.getStatus());
        categoryMapper.insert(category);
        return new CategoryVO(category);
    }

    @Override
    public CategoryVO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setStatus(categoryDTO.getStatus());
        categoryMapper.updateById(category);
        return new CategoryVO(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        // 先删除分类与帖子的关联
        QueryWrapper<PostCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", id);
        postCategoryMapper.delete(wrapper);
        // 再删除分类
        return categoryMapper.deleteById(id) > 0;
    }

    @Override
    public List<CategoryVO> getCategoriesByPostId(Long postId) {
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
        
        List<Category> categories = categoryMapper.selectBatchIds(categoryIds);
        return categories.stream()
                .map(CategoryVO::new)
                .collect(Collectors.toList());
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