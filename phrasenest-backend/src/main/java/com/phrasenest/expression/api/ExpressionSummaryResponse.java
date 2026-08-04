package com.phrasenest.expression.api;


import com.phrasenest.expression.domain.ExpressionType;

import java.util.UUID;

/**
 * Lightweight expression response used in lists and category pages.
 *
 * It intentionally excludes long fields such as:
 * - detailed meaning
 * - literal meaning
 * - usage notes
 */
public record ExpressionSummaryResponse(

        UUID id,

        String canonicalText,

        String slug,

        ExpressionType expressionType,

        String shortMeaning,

        String cefrLevel,

        String formality,

        String offensiveLevel
) {
}