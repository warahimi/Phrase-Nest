package com.phrasenest.expression.example.api;


import com.phrasenest.expression.example.domain.Appropriateness;
import com.phrasenest.expression.example.domain.DifficultyLevel;
import com.phrasenest.expression.example.domain.ExampleType;

import java.time.Instant;
import java.util.UUID;

public record ExpressionExampleResponse(
        UUID id,
        UUID expressionId,
        String exampleText,
        String contextText,
        ExampleType exampleType,
        DifficultyLevel difficultyLevel,
        Appropriateness appropriateness,
        String explanation,
        int displayOrder,
        Instant createdAt,
        Instant updatedAt
) {
}