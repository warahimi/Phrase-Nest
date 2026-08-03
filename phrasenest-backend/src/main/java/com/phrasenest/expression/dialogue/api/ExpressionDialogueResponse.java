package com.phrasenest.expression.dialogue.api;


import com.phrasenest.expression.example.domain.DifficultyLevel;

import java.time.Instant;
import java.util.UUID;

public record ExpressionDialogueResponse(
        UUID id,
        UUID expressionId,
        String speakerAName,
        String speakerAText,
        String speakerBName,
        String speakerBText,
        String contextText,
        DifficultyLevel difficultyLevel,
        int displayOrder,
        Instant createdAt,
        Instant updatedAt
) {
}
