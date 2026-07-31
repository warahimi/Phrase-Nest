package com.phrasenest.expression.alias.api;


import com.phrasenest.expression.alias.domain.AliasType;
import com.phrasenest.expression.api.ExpressionResponse;

/**
 * Response returned when a user's query matches an alias.
 */
public record AliasResolutionResponse(

        /**
         * The exact text entered or matched.
         */
        String matchedAlias,

        /**
         * Whether the alias is a correct alternative.
         */
        boolean correct,

        /**
         * The kind of alias that matched.
         */
        AliasType aliasType,

        /**
         * Optional correction or usage explanation.
         */
        String usageNote,

        /**
         * The main canonical expression.
         */
        ExpressionResponse expression
) {
}