package com.phrasenest.expression.example.api;


import com.phrasenest.expression.example.domain.Appropriateness;
import com.phrasenest.expression.example.domain.DifficultyLevel;
import com.phrasenest.expression.example.domain.ExampleType;
import jakarta.validation.constraints.*;

/**
 * Request used when creating an example sentence.
 */
public record CreateExpressionExampleRequest(

        @NotBlank(message = "Example text is required.")
        @Size(
                max = 2000,
                message = "Example text cannot exceed 2000 characters."
        )
        String exampleText,

        @Size(
                max = 2000,
                message = "Context cannot exceed 2000 characters."
        )
        String contextText,

        @NotNull(message = "Example type is required.")
        ExampleType exampleType,

        DifficultyLevel difficultyLevel,

        @NotNull(message = "Appropriateness is required.")
        Appropriateness appropriateness,

        @Size(
                max = 3000,
                message = "Explanation cannot exceed 3000 characters."
        )
        String explanation,

        @Min(
                value = 0,
                message = "Display order cannot be negative."
        )
        @Max(
                value = 10000,
                message = "Display order is too large."
        )
        int displayOrder
) {
}
