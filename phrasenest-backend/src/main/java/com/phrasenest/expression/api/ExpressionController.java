package com.phrasenest.expression.api;


import com.phrasenest.expression.application.ExpressionService;
import com.phrasenest.shared.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST endpoints for managing expressions.
 *
 * This controller is currently under /admin because expression creation,
 * updating, publishing, and deletion should not be public actions.
 */
@RestController
@RequestMapping("/api/v1/admin/expressions")
public class ExpressionController {

    private final ExpressionService expressionService;

    public ExpressionController(
            ExpressionService expressionService
    ) {
        this.expressionService = expressionService;
    }

    /**
     * POST /api/v1/admin/expressions
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ExpressionResponse>> create(
            @Valid @RequestBody CreateExpressionRequest request
    ) {
        ExpressionResponse response =
                expressionService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Expression created successfully.",
                        response
                ));
    }

    /**
     * GET /api/v1/admin/expressions/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<ExpressionResponse> getById(
            @PathVariable UUID id
    ) {
        return ApiResponse.success(
                "Expression retrieved successfully.",
                expressionService.getById(id)
        );
    }

    /**
     * PUT /api/v1/admin/expressions/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<ExpressionResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateExpressionRequest request
    ) {
        return ApiResponse.success(
                "Expression updated successfully.",
                expressionService.update(id, request)
        );
    }

    /**
     * PATCH /api/v1/admin/expressions/{id}/publication-status
     */
    @PatchMapping("/{id}/publication-status")
    public ApiResponse<ExpressionResponse> changePublicationStatus(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            ChangePublicationStatusRequest request
    ) {
        return ApiResponse.success(
                "Publication status changed successfully.",
                expressionService.changePublicationStatus(
                        id,
                        request
                )
        );
    }

    /**
     * DELETE /api/v1/admin/expressions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        expressionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}