package com.phrasenest.expression.example.application;


import com.phrasenest.expression.example.api.ExpressionExampleResponse;
import com.phrasenest.expression.example.domain.ExpressionExample;
import org.springframework.stereotype.Component;

/**
 * Converts example entities into API responses.
 */
@Component
public class ExpressionExampleMapper {

    public ExpressionExampleResponse toResponse(
            ExpressionExample example
    ) {
        return new ExpressionExampleResponse(
                example.getId(),
                example.getExpression().getId(),
                example.getExampleText(),
                example.getContextText(),
                example.getExampleType(),
                example.getDifficultyLevel(),
                example.getAppropriateness(),
                example.getExplanation(),
                example.getDisplayOrder(),
                example.getCreatedAt(),
                example.getUpdatedAt()
        );
    }
}