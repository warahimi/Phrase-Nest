package com.phrasenest.expression.category.api;


import java.time.Instant;
import java.util.UUID;

/**
 * Information about one expression-category assignment.
 */
public record ExpressionCategoryResponse(

        UUID expressionId,

        String expressionCanonicalText,

        UUID categoryId,

        String categoryName,

        String categorySlug,

        boolean primary,

        Instant assignedAt
) {
}
