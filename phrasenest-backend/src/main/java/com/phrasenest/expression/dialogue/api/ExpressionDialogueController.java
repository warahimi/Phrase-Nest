package com.phrasenest.expression.dialogue.api;


import com.phrasenest.expression.dialogue.application.ExpressionDialogueService;
import com.phrasenest.shared.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
public class ExpressionDialogueController {

    private final ExpressionDialogueService dialogueService;

    public ExpressionDialogueController(ExpressionDialogueService dialogueService)
    {
        this.dialogueService = dialogueService;
    }

    @PostMapping("/expressions/{expressionId}/dialogues")
    public ResponseEntity<ApiResponse<ExpressionDialogueResponse>> create(
            @PathVariable UUID expressionId,
            @Valid
            @RequestBody
            CreateExpressionDialogueRequest request)
    {
        System.out.println(request);
        System.out.println("Dialogue API triggered");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Expression dialogue created successfully.",
                        dialogueService.create(expressionId, request)
                ));
    }

    @GetMapping("/expressions/{expressionId}/dialogues")
    public ApiResponse<List<ExpressionDialogueResponse>>
    getAllForExpression(
            @PathVariable UUID expressionId
    ) {
        return ApiResponse.success(
                "Expression dialogues retrieved successfully.",
                dialogueService.getAllForExpression(expressionId)
        );
    }

    @PutMapping("/dialogues/{dialogueId}")
    public ApiResponse<ExpressionDialogueResponse> update(
            @PathVariable UUID dialogueId,
            @Valid
            @RequestBody
            UpdateExpressionDialogueRequest request
    ) {
        return ApiResponse.success(
                "Expression dialogue updated successfully.",
                dialogueService.update(dialogueId, request)
        );
    }

    @DeleteMapping("/dialogues/{dialogueId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID dialogueId
    ) {
        dialogueService.delete(dialogueId);
        return ResponseEntity.noContent().build();
    }
}