package com.phrasenest.expression.category.infrastructure;


import com.phrasenest.expression.category.domain.ExpressionCategory;
import com.phrasenest.expression.category.domain.ExpressionCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Database access for expression-category assignments.
 */
public interface ExpressionCategoryRepository
        extends JpaRepository<
        ExpressionCategory,
        ExpressionCategoryId
        > {

    /**
     * Returns every category assigned to an expression.
     */
    @Query("""
            SELECT assignment
            FROM ExpressionCategory assignment
            JOIN FETCH assignment.category category
            WHERE assignment.expression.id = :expressionId
            ORDER BY assignment.primary DESC,
                     category.displayOrder ASC,
                     category.name ASC
            """)
    List<ExpressionCategory>
    findAllWithCategoryByExpressionId(
            @Param("expressionId") UUID expressionId
    );

    /**
     * Returns every expression assigned to a category.
     */
    @Query("""
            SELECT assignment
            FROM ExpressionCategory assignment
            JOIN FETCH assignment.expression expression
            WHERE assignment.category.id = :categoryId
              AND expression.publicationStatus =
                  com.phrasenest.expression.domain.PublicationStatus.PUBLISHED
            ORDER BY expression.canonicalText ASC
            """)
    List<ExpressionCategory>
    findAllPublishedWithExpressionByCategoryId(
            @Param("categoryId") UUID categoryId
    );

    /**
     * Finds one assignment.
     */
    Optional<ExpressionCategory>
    findByExpressionIdAndCategoryId(
            UUID expressionId,
            UUID categoryId
    );

    /**
     * Checks whether an assignment already exists.
     */
    boolean existsByExpressionIdAndCategoryId(
            UUID expressionId,
            UUID categoryId
    );

    /**
     * Checks whether the category is used by any expression.
     */
    boolean existsByCategoryId(UUID categoryId);

    /**
     * Checks whether the expression already has a primary category.
     */
    boolean existsByExpressionIdAndPrimaryTrue(
            UUID expressionId
    );

    /**
     * Returns the primary category assignment.
     */
    Optional<ExpressionCategory>
    findByExpressionIdAndPrimaryTrue(
            UUID expressionId
    );

    /**
     * Deletes all category assignments for an expression.
     */
    void deleteAllByExpressionId(UUID expressionId);
}