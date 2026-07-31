package com.phrasenest.expression.example.api;


import com.phrasenest.expression.example.domain.Appropriateness;
import com.phrasenest.expression.example.domain.DifficultyLevel;
import com.phrasenest.expression.example.domain.ExampleType;
import jakarta.validation.constraints.*;

public record UpdateExpressionExampleRequest(

        @NotBlank(message = "Example text is required.")
        @Size(max = 2000)
        String exampleText,

        @Size(max = 2000)
        String contextText,

        @NotNull(message = "Example type is required.")
        ExampleType exampleType,

        DifficultyLevel difficultyLevel,

        @NotNull(message = "Appropriateness is required.")
        Appropriateness appropriateness,

        @Size(max = 3000)
        String explanation,

        @Min(0)
        @Max(10000)
        int displayOrder
) {
}
