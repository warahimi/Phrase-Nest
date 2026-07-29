package com.phrasenest.expression.application;

import com.phrasenest.expression.api.ExpressionResponse;
import com.phrasenest.expression.domain.Expression;
import org.springframework.stereotype.Component;

/**
 * Converts domain entities into API response DTOs.
 *
 * We are writing this manually first so the mapping is easy to understand.
 * MapStruct can be introduced later if mapping becomes repetitive.
 */
@Component
public class ExpressionMapper {

    public ExpressionResponse toResponse(Expression expression) {
        return new ExpressionResponse(
                expression.getId(),
                expression.getCanonicalText(),
                expression.getNormalizedText(),
                expression.getSlug(),
                expression.getExpressionType(),
                expression.getShortMeaning(),
                expression.getDetailedMeaning(),
                expression.getLiteralMeaning(),
                expression.getUsageNotes(),
                expression.getCefrLevel(),
                expression.getFormality(),
                expression.getTone(),
                expression.getFrequencyLevel(),
                expression.getRegion(),
                expression.getOffensiveLevel(),
                expression.getPublicationStatus(),
                expression.getSourceType(),
                expression.getConfidenceScore(),
                expression.isFeatured(),
                expression.getViewCount(),
                expression.getSearchCount(),
                expression.getSaveCount(),
                expression.getCreatedAt(),
                expression.getUpdatedAt()
        );
    }
}
