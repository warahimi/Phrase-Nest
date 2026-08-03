package com.phrasenest.expression.dialogue.api;


import com.phrasenest.expression.example.domain.DifficultyLevel;
import jakarta.validation.constraints.*;

public record CreateExpressionDialogueRequest(

        @Size(max = 100)
        String speakerAName,

        @NotBlank(message = "Speaker A text is required.")
        @Size(max = 2000)
        String speakerAText,

        @Size(max = 100)
        String speakerBName,

        @NotBlank(message = "Speaker B text is required.")
        @Size(max = 2000)
        String speakerBText,

        @Size(max = 2000)
        String contextText,

        DifficultyLevel difficultyLevel,

        @Min(0)
        @Max(10000)
        int displayOrder
) {
}