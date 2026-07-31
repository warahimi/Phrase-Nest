package com.phrasenest.expression.example.infrastructure;


import com.phrasenest.expression.example.domain.ExpressionExample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Database operations for expression examples.
 */
public interface ExpressionExampleRepository
        extends JpaRepository<ExpressionExample, UUID> {

    /**
     * Returns examples in the order selected by the administrator.
     */
    List<ExpressionExample>
    findAllByExpressionIdOrderByDisplayOrderAscCreatedAtAsc(
            UUID expressionId
    );

    /**
     * Checks whether a particular example text already exists
     * for the same expression.
     */
    boolean existsByExpressionIdAndExampleTextIgnoreCase(
            UUID expressionId,
            String exampleText
    );

    /**
     * Used during an update so the current example is excluded.
     */
    boolean existsByExpressionIdAndExampleTextIgnoreCaseAndIdNot(
            UUID expressionId,
            String exampleText,
            UUID exampleId
    );
}