package com.phrasenest.expression.api;

import com.phrasenest.expression.domain.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Data returned to the frontend.
 *
 * We return a DTO instead of returning the JPA entity directly.
 */
public record ExpressionResponse(
        UUID id,
        String canonicalText,
        String normalizedText,
        String slug,
        ExpressionType expressionType,
        String shortMeaning,
        String detailedMeaning,
        String literalMeaning,
        String usageNotes,
        String cefrLevel,
        Formality formality,
        String tone,
        FrequencyLevel frequencyLevel,
        String region,
        OffensiveLevel offensiveLevel,
        PublicationStatus publicationStatus,
        SourceType sourceType,
        Double confidenceScore,
        boolean featured,
        long viewCount,
        long searchCount,
        long saveCount,
        Instant createdAt,
        Instant updatedAt
) {
}