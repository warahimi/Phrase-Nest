package com.phrasenest.category.api;


import com.phrasenest.category.application.CategoryService;
import com.phrasenest.shared.api.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public read-only category endpoints.
 */
@RestController
@RequestMapping("/api/v1/public/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    public PublicCategoryController(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    /**
     * Returns the active category hierarchy.
     *
     * GET /api/v1/public/categories/tree
     */
    @GetMapping("/tree")
    public ApiResponse<List<CategoryTreeResponse>>
    getCategoryTree() {

        return ApiResponse.success(
                "Category tree retrieved successfully.",
                categoryService.getActiveCategoryTree()
        );
    }

    /**
     * Returns an active category by slug.
     *
     * GET /api/v1/public/categories/workplace
     */
    @GetMapping("/{slug}")
    public ApiResponse<CategoryResponse> getBySlug(
            @PathVariable String slug
    ) {
        return ApiResponse.success(
                "Category retrieved successfully.",
                categoryService.getActiveBySlug(slug)
        );
    }
}
