package com.loafer.blog.service;

import com.loafer.blog.model.dto.CategoryDTO;
import com.loafer.blog.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    List<CategoryVO> getAllCategories();
    CategoryVO createCategory(CategoryDTO categoryDTO);
    CategoryVO updateCategory(Long id, CategoryDTO categoryDTO);
    boolean deleteCategory(Long id);
    List<CategoryVO> getCategoriesByPostId(Long postId);
    void addCategoryToPost(Long postId, List<Long> categoryIds);
}
