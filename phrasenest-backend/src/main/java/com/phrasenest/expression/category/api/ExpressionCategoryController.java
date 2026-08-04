package com.phrasenest.expression.category.api;


import com.phrasenest.expression.category.application.ExpressionCategoryService;
import com.phrasenest.shared.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Administrative endpoints for managing expression categories.
 */
@RestController
@RequestMapping("/api/v1/admin/expressions")
public class ExpressionCategoryController {

    private final ExpressionCategoryService categoryService;

    public ExpressionCategoryController(
            ExpressionCategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    /**
     * Assigns a category to an expression.
     *
     * POST /api/v1/admin/expressions/{expressionId}/categories
     */
    @PostMapping("/{expressionId}/categories")
    public ResponseEntity<ApiResponse<ExpressionCategoryResponse>>
    assignCategory(
            @PathVariable UUID expressionId,
            @Valid
            @RequestBody
            AssignExpressionCategoryRequest request
    ) {
        ExpressionCategoryResponse response =
                categoryService.assign(expressionId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Category assigned to expression successfully.",
                        response
                ));
    }

    /**
     * Returns all categories assigned to an expression.
     *
     * GET /api/v1/admin/expressions/{expressionId}/categories
     */
    @GetMapping("/{expressionId}/categories")
    public ApiResponse<List<ExpressionCategoryResponse>>
    getCategories(
            @PathVariable UUID expressionId
    ) {
        return ApiResponse.success(
                "Expression categories retrieved successfully.",
                categoryService.getCategoriesForExpression(
                        expressionId
                )
        );
    }

    /**
     * Makes one assigned category the primary category.
     *
     * PATCH
     * /api/v1/admin/expressions/{expressionId}/categories/{categoryId}/primary
     */
    @PatchMapping(
            "/{expressionId}/categories/{categoryId}/primary"
    )
    public ApiResponse<ExpressionCategoryResponse>
    makePrimary(
            @PathVariable UUID expressionId,
            @PathVariable UUID categoryId
    ) {
        return ApiResponse.success(
                "Primary category changed successfully.",
                categoryService.makePrimary(
                        expressionId,
                        categoryId
                )
        );
    }

    /**
     * Removes a category assignment.
     *
     * DELETE
     * /api/v1/admin/expressions/{expressionId}/categories/{categoryId}
     */
    @DeleteMapping(
            "/{expressionId}/categories/{categoryId}"
    )
    public ResponseEntity<Void> removeCategory(
            @PathVariable UUID expressionId,
            @PathVariable UUID categoryId
    ) {
        categoryService.remove(
                expressionId,
                categoryId
        );

        return ResponseEntity.noContent().build();
    }
}
