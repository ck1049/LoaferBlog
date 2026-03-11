package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final PostCategoryMapper postCategoryMapper;

    public CategoryServiceImpl(PostCategoryMapper postCategoryMapper) {
        this.postCategoryMapper = postCategoryMapper;
    }

    @Override
    public List<CategoryVO> getAllCategories() {
        List<Category> categories = list();
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
        save(category);
        return new CategoryVO(category);
    }

    @Override
    public CategoryVO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = getById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setStatus(categoryDTO.getStatus());
        updateById(category);
        return new CategoryVO(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        // 先删除分类与帖子的关联
        QueryWrapper<PostCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", id);
        postCategoryMapper.delete(wrapper);
        // 再删除分类
        return removeById(id);
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
        
        List<Category> categories = listByIds(categoryIds);
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