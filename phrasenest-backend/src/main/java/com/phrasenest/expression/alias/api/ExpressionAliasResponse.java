package com.phrasenest.expression.alias.api;


import com.phrasenest.expression.alias.domain.AliasType;

import java.time.Instant;
import java.util.UUID;

/**
 * Alias data returned to the frontend.
 */
public record ExpressionAliasResponse(
        UUID id,
        UUID expressionId,
        String expressionCanonicalText,
        String aliasText,
        String normalizedAlias,
        AliasType aliasType,
        boolean correct,
        String usageNote,
        Instant createdAt,
        Instant updatedAt
) {
}
