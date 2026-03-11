package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.dto.CategoryDTO;
import com.loafer.blog.model.entity.Category;
import com.loafer.blog.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVO> getAllCategories();
    CategoryVO createCategory(CategoryDTO categoryDTO);
    CategoryVO updateCategory(Long id, CategoryDTO categoryDTO);
    boolean deleteCategory(Long id);
    List<CategoryVO> getCategoriesByPostId(Long postId);
    void addCategoryToPost(Long postId, List<Long> categoryIds);
}
