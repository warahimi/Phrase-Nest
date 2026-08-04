package com.phrasenest.expression.category.api;


import com.phrasenest.expression.category.application.ExpressionCategoryService;
import com.phrasenest.shared.api.ApiResponse;
import org.springframework.web.bind.annotation.*;

/**
 * Public endpoints for browsing expressions by category.
 */
@RestController
@RequestMapping("/api/v1/public/categories")
public class PublicCategoryExpressionController {

    private final ExpressionCategoryService categoryService;

    public PublicCategoryExpressionController(
            ExpressionCategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    /**
     * Returns one category and its published expressions.
     *
     * Example:
     *
     * GET /api/v1/public/categories/agreement/expressions
     */
    @GetMapping("/{categorySlug}/expressions")
    public ApiResponse<CategoryExpressionsResponse>
    getExpressionsByCategory(
            @PathVariable String categorySlug
    ) {
        return ApiResponse.success(
                "Category expressions retrieved successfully.",
                categoryService
                        .getPublishedExpressionsByCategorySlug(
                                categorySlug
                        )
        );
    }
}
