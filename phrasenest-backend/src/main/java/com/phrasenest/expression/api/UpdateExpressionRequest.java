package com.phrasenest.expression.api;

import com.phrasenest.expression.domain.ExpressionType;
import com.phrasenest.expression.domain.Formality;
import com.phrasenest.expression.domain.FrequencyLevel;
import com.phrasenest.expression.domain.OffensiveLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request for updating the main educational content.
 *
 * Publication status is deliberately excluded.
 * We will provide a separate endpoint for publishing and archiving.
 */
public record UpdateExpressionRequest(

        @NotBlank(message = "Canonical text is required.")
        @Size(max = 500)
        String canonicalText,

        @NotNull(message = "Expression type is required.")
        ExpressionType expressionType,

        @NotBlank(message = "Short meaning is required.")
        @Size(max = 1000)
        String shortMeaning,

        @Size(max = 5000)
        String detailedMeaning,

        @Size(max = 3000)
        String literalMeaning,

        @Size(max = 5000)
        String usageNotes,

        @Pattern(
                regexp = "A1|A2|B1|B2|C1|C2",
                message = "CEFR level must be A1, A2, B1, B2, C1, or C2."
        )
        String cefrLevel,

        Formality formality,

        @Size(max = 100)
        String tone,

        FrequencyLevel frequencyLevel,

        @Size(max = 50)
        String region,

        @NotNull(message = "Offensive level is required.")
        OffensiveLevel offensiveLevel
) {
}
