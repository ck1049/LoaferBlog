package com.loafer.blog.controller;

import com.loafer.blog.model.dto.CategoryDTO;
import com.loafer.blog.model.vo.CategoryVO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseVO<List<CategoryVO>> getAllCategories() {
        List<CategoryVO> categories = categoryService.getAllCategories();
        return ResponseVO.success(categories);
    }

    @GetMapping("/post/{postId}")
    public ResponseVO<List<CategoryVO>> getCategoriesByPostId(@PathVariable Long postId) {
        List<CategoryVO> categories = categoryService.getCategoriesByPostId(postId);
        return ResponseVO.success(categories);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<CategoryVO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryVO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseVO.success(createdCategory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<CategoryVO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryVO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseVO.success(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<Void> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseVO.success();
        } else {
            return ResponseVO.error("分类不存在");
        }
    }

    @PostMapping("/post/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseVO<Void> addCategoryToPost(@PathVariable Long postId, @RequestBody List<Long> categoryIds) {
        categoryService.addCategoryToPost(postId, categoryIds);
        return ResponseVO.success();
    }
}