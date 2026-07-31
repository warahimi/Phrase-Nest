package com.phrasenest.expression.alias.infrastructure;


import com.phrasenest.expression.alias.domain.ExpressionAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for storing and searching expression aliases.
 */
public interface ExpressionAliasRepository
        extends JpaRepository<ExpressionAlias, UUID> {

    /**
     * Returns all aliases belonging to one expression.
     */
    List<ExpressionAlias> findAllByExpressionIdOrderByCreatedAtAsc(
            UUID expressionId
    );

    /**
     * Finds an exact normalized alias.
     *
     * Example:
     * Input: "i was not born yesterday"
     */
    Optional<ExpressionAlias> findByNormalizedAlias(
            String normalizedAlias
    );

    /**
     * Checks whether an alias already exists.
     */
    boolean existsByNormalizedAlias(String normalizedAlias);

    /**
     * Used during update so the current alias is not considered a duplicate.
     */
    boolean existsByNormalizedAliasAndIdNot(
            String normalizedAlias,
            UUID id
    );

    /**
     * Checks whether an alias already belongs to the expression.
     */
    boolean existsByExpressionIdAndNormalizedAlias(
            UUID expressionId,
            String normalizedAlias
    );

    /**
     * Fetches the alias and associated expression in one query.
     *
     * JOIN FETCH avoids an additional database query when we need
     * both the alias and its expression.
     */
    @Query("""
            SELECT alias
            FROM ExpressionAlias alias
            JOIN FETCH alias.expression expression
            WHERE alias.normalizedAlias = :normalizedAlias
            """)
    Optional<ExpressionAlias> findWithExpressionByNormalizedAlias(
            @Param("normalizedAlias") String normalizedAlias
    );

    /**
     * Deletes all aliases associated with an expression.
     *
     * Normally the database cascade handles this when the expression
     * itself is deleted.
     */
    void deleteAllByExpressionId(UUID expressionId);
}
