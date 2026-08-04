package com.phrasenest.expression.category.application;

import com.phrasenest.expression.api.ExpressionSummaryResponse;
import com.phrasenest.expression.category.api.ExpressionCategoryResponse;
import com.phrasenest.expression.category.domain.ExpressionCategory;
import com.phrasenest.expression.domain.Expression;
import org.springframework.stereotype.Component;

/**
 * Converts expression-category assignments into API responses.
 */
@Component
public class ExpressionCategoryMapper {

    public ExpressionCategoryResponse toResponse(
            ExpressionCategory assignment
    ) {
        return new ExpressionCategoryResponse(
                assignment.getExpression().getId(),
                assignment.getExpression().getCanonicalText(),
                assignment.getCategory().getId(),
                assignment.getCategory().getName(),
                assignment.getCategory().getSlug(),
                assignment.isPrimary(),
                assignment.getAssignedAt()
        );
    }

    /**
     * Creates a lightweight expression response for category browsing.
     */
    public ExpressionSummaryResponse toExpressionSummary(
            Expression expression
    ) {
        return new ExpressionSummaryResponse(
                expression.getId(),
                expression.getCanonicalText(),
                expression.getSlug(),
                expression.getExpressionType(),
                expression.getShortMeaning(),
                expression.getCefrLevel(),

                /*
                 * Enum values are converted to text for this compact DTO.
                 */
                expression.getFormality() == null
                        ? null
                        : expression.getFormality().name(),

                expression.getOffensiveLevel() == null
                        ? null
                        : expression.getOffensiveLevel().name()
        );
    }
}
