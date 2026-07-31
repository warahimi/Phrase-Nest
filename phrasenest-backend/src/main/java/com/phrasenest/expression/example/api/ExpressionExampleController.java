package com.phrasenest.expression.example.api;


import com.phrasenest.expression.example.application.ExpressionExampleService;
import com.phrasenest.shared.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Administrative endpoints for creating and editing examples.
 */
@RestController
@RequestMapping("/api/v1/admin")
public class ExpressionExampleController {

    private final ExpressionExampleService exampleService;

    public ExpressionExampleController(
            ExpressionExampleService exampleService
    ) {
        this.exampleService = exampleService;
    }

    @PostMapping("/expressions/{expressionId}/examples")
    public ResponseEntity<ApiResponse<ExpressionExampleResponse>>
    create(
            @PathVariable UUID expressionId,
            @Valid
            @RequestBody
            CreateExpressionExampleRequest request
    ) {
        ExpressionExampleResponse response =
                exampleService.create(expressionId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Expression example created successfully.",
                        response
                ));
    }

    @GetMapping("/expressions/{expressionId}/examples")
    public ApiResponse<List<ExpressionExampleResponse>>
    getAllForExpression(
            @PathVariable UUID expressionId
    ) {
        return ApiResponse.success(
                "Expression examples retrieved successfully.",
                exampleService.getAllForExpression(expressionId)
        );
    }

    @GetMapping("/examples/{exampleId}")
    public ApiResponse<ExpressionExampleResponse> getById(
            @PathVariable UUID exampleId
    ) {
        return ApiResponse.success(
                "Expression example retrieved successfully.",
                exampleService.getById(exampleId)
        );
    }

    @PutMapping("/examples/{exampleId}")
    public ApiResponse<ExpressionExampleResponse> update(
            @PathVariable UUID exampleId,
            @Valid
            @RequestBody
            UpdateExpressionExampleRequest request
    ) {
        return ApiResponse.success(
                "Expression example updated successfully.",
                exampleService.update(exampleId, request)
        );
    }

    @DeleteMapping("/examples/{exampleId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID exampleId
    ) {
        exampleService.delete(exampleId);
        return ResponseEntity.noContent().build();
    }
}