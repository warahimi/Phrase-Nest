package com.phrasenest.expression.api;


import com.phrasenest.expression.application.ExpressionService;
import com.phrasenest.shared.api.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * Read-only endpoints available to application visitors.
 */
@RestController
@RequestMapping("/api/v1/public/expressions")
public class PublicExpressionController {

    private final ExpressionService expressionService;

    public PublicExpressionController(
            ExpressionService expressionService
    ) {
        this.expressionService = expressionService;
    }

    /**
     * Example:
     *
     * GET /api/v1/public/expressions?page=0&size=20&sort=createdAt,desc
     */
    @GetMapping
    public ApiResponse<Page<ExpressionResponse>> getPublished(
            Pageable pageable
    ) {
        return ApiResponse.success(
                "Published expressions retrieved successfully.",
                expressionService.getPublished(pageable)
        );
    }

    /**
     * Example:
     *
     * GET /api/v1/public/expressions/i-wasnt-born-yesterday
     */
//    @GetMapping("/{slug}")
//    public ApiResponse<ExpressionResponse> getBySlug(
//            @PathVariable String slug
//    ) {
//        return ApiResponse.success(
//                "Expression retrieved successfully.",
//                expressionService.getBySlug(slug)
//        );
//    }
    @GetMapping("/{slug}")
    public ApiResponse<ExpressionResponse> getBySlug(
            @PathVariable String slug
    ) {
        return ApiResponse.success(
                "Expression retrieved successfully.",
                expressionService.getPublishedBySlug(slug)
        );
    }
}