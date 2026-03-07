package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getAllCategories();
    Category createCategory(Category category);
    Category updateCategory(Category category);
    boolean deleteCategory(Long id);
    List<Category> getCategoriesByPostId(Long postId);
    void addCategoryToPost(Long postId, List<Long> categoryIds);
}
