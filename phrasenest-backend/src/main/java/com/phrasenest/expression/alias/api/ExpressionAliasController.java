package com.phrasenest.expression.alias.api;


import com.phrasenest.expression.alias.application.ExpressionAliasService;
import com.phrasenest.shared.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Administrative endpoints for managing aliases.
 */
@RestController
@RequestMapping("/api/v1/admin")
public class ExpressionAliasController {

    private final ExpressionAliasService aliasService;

    public ExpressionAliasController(
            ExpressionAliasService aliasService
    ) {
        this.aliasService = aliasService;
    }

    /**
     * Adds an alias to an expression.
     *
     * POST /api/v1/admin/expressions/{expressionId}/aliases
     */
    @PostMapping("/expressions/{expressionId}/aliases")
    public ResponseEntity<ApiResponse<ExpressionAliasResponse>>
    createAlias(
            @PathVariable UUID expressionId,
            @Valid
            @RequestBody
            CreateExpressionAliasRequest request
    ) {
        ExpressionAliasResponse response =
                aliasService.create(expressionId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Expression alias created successfully.",
                        response
                ));
    }

    /**
     * Lists every alias belonging to an expression.
     *
     * GET /api/v1/admin/expressions/{expressionId}/aliases
     */
    @GetMapping("/expressions/{expressionId}/aliases")
    public ApiResponse<List<ExpressionAliasResponse>>
    getAliasesForExpression(
            @PathVariable UUID expressionId
    ) {
        return ApiResponse.success(
                "Expression aliases retrieved successfully.",
                aliasService.getAllForExpression(expressionId)
        );
    }

    /**
     * Gets one alias.
     *
     * GET /api/v1/admin/aliases/{aliasId}
     */
    @GetMapping("/aliases/{aliasId}")
    public ApiResponse<ExpressionAliasResponse> getAlias(
            @PathVariable UUID aliasId
    ) {
        return ApiResponse.success(
                "Expression alias retrieved successfully.",
                aliasService.getById(aliasId)
        );
    }

    /**
     * Updates one alias.
     *
     * PUT /api/v1/admin/aliases/{aliasId}
     */
    @PutMapping("/aliases/{aliasId}")
    public ApiResponse<ExpressionAliasResponse> updateAlias(
            @PathVariable UUID aliasId,
            @Valid
            @RequestBody
            UpdateExpressionAliasRequest request
    ) {
        return ApiResponse.success(
                "Expression alias updated successfully.",
                aliasService.update(aliasId, request)
        );
    }

    /**
     * Deletes one alias.
     *
     * DELETE /api/v1/admin/aliases/{aliasId}
     */
    @DeleteMapping("/aliases/{aliasId}")
    public ResponseEntity<Void> deleteAlias(
            @PathVariable UUID aliasId
    ) {
        aliasService.delete(aliasId);

        return ResponseEntity.noContent().build();
    }
}
