package com.phrasenest.category.api;


import com.phrasenest.category.application.CategoryService;
import com.phrasenest.shared.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Administrative category endpoints.
 */
@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    /**
     * Creates a new category.
     *
     * POST /api/v1/admin/categories
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @Valid
            @RequestBody
            CreateCategoryRequest request
    ) {
        CategoryResponse response =
                categoryService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Category created successfully.",
                        response
                ));
    }

    /**
     * Returns all categories, including inactive categories.
     *
     * GET /api/v1/admin/categories
     */
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll() {
        return ApiResponse.success(
                "Categories retrieved successfully.",
                categoryService.getAll()
        );
    }

    /**
     * Returns one category.
     *
     * GET /api/v1/admin/categories/{categoryId}
     */
    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> getById(
            @PathVariable UUID categoryId
    ) {
        return ApiResponse.success(
                "Category retrieved successfully.",
                categoryService.getById(categoryId)
        );
    }

    /**
     * Updates one category.
     *
     * PUT /api/v1/admin/categories/{categoryId}
     */
    @PutMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> update(
            @PathVariable UUID categoryId,
            @Valid
            @RequestBody
            UpdateCategoryRequest request
    ) {
        return ApiResponse.success(
                "Category updated successfully.",
                categoryService.update(categoryId, request)
        );
    }

    /**
     * Permanently deletes a category.
     *
     * DELETE /api/v1/admin/categories/{categoryId}
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID categoryId
    ) {
        categoryService.delete(categoryId);

        return ResponseEntity.noContent().build();
    }
}
