package com.phrasenest.expression.api;

import com.phrasenest.expression.domain.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data accepted when an administrator creates an expression.
 *
 * We do not accept normalizedText or slug from the client.
 * The backend generates those values to keep them consistent.
 *
 * Example request:
 * {
 *   "canonicalText": "I wasn't born yesterday.",
 *   "expressionType": "IDIOM",
 *   "shortMeaning": "I am not naive or easily fooled.",
 *   "detailedMeaning": "The speaker is saying that they have enough experience to recognize dishonesty.",
 *   "literalMeaning": "The speaker was not actually born one day ago.",
 *   "usageNotes": "Often used when someone believes another person is trying to deceive them.",
 *   "cefrLevel": "B2",
 *   "formality": "INFORMAL",
 *   "tone": "ASSERTIVE",
 *   "frequencyLevel": "COMMON",
 *   "region": "WIDELY_USED",
 *   "offensiveLevel": "NONE",
 *   "publicationStatus": "PUBLISHED",
 *   "sourceType": "EDITORIAL",
 *   "confidenceScore": 1.0
 * }
 */
public record CreateExpressionRequest(

        @NotBlank(message = "Canonical text is required.")
        @Size(
                max = 500,
                message = "Canonical text cannot exceed 500 characters."
        )
        String canonicalText,

        @NotNull(message = "Expression type is required.")
        ExpressionType expressionType,

        @NotBlank(message = "A short meaning is required.")
        @Size(
                max = 1000,
                message = "Short meaning cannot exceed 1000 characters."
        )
        String shortMeaning,

        @Size(
                max = 5000,
                message = "Detailed meaning cannot exceed 5000 characters."
        )
        String detailedMeaning,

        @Size(
                max = 3000,
                message = "Literal meaning cannot exceed 3000 characters."
        )
        String literalMeaning,

        @Size(
                max = 5000,
                message = "Usage notes cannot exceed 5000 characters."
        )
        String usageNotes,

        /**
         * Allows null or one of the valid CEFR levels.
         */
        @Pattern(
                regexp = "A1|A2|B1|B2|C1|C2",
                message = "CEFR level must be A1, A2, B1, B2, C1, or C2."
        )
        String cefrLevel,

        Formality formality,

        @Size(
                max = 100,
                message = "Tone cannot exceed 100 characters."
        )
        String tone,

        FrequencyLevel frequencyLevel,

        @Size(
                max = 50,
                message = "Region cannot exceed 50 characters."
        )
        String region,

        @NotNull(message = "Offensive level is required.")
        OffensiveLevel offensiveLevel,

        @NotNull(message = "Publication status is required.")
        PublicationStatus publicationStatus,

        @NotNull(message = "Source type is required.")
        SourceType sourceType,

        @DecimalMin(
                value = "0.0",
                message = "Confidence score cannot be lower than 0."
        )
        @DecimalMax(
                value = "1.0",
                message = "Confidence score cannot be greater than 1."
        )
        Double confidenceScore
) {
}