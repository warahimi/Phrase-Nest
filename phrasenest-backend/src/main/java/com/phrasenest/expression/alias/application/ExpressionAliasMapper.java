package com.phrasenest.expression.alias.application;


import com.phrasenest.expression.alias.api.ExpressionAliasResponse;
import com.phrasenest.expression.alias.domain.ExpressionAlias;
import org.springframework.stereotype.Component;

/**
 * Converts ExpressionAlias entities into API DTOs.
 */
@Component
public class ExpressionAliasMapper {

    public ExpressionAliasResponse toResponse(
            ExpressionAlias alias
    ) {
        return new ExpressionAliasResponse(
                alias.getId(),
                alias.getExpression().getId(),
                alias.getExpression().getCanonicalText(),
                alias.getAliasText(),
                alias.getNormalizedAlias(),
                alias.getAliasType(),
                alias.isCorrect(),
                alias.getUsageNote(),
                alias.getCreatedAt(),
                alias.getUpdatedAt()
        );
    }
}