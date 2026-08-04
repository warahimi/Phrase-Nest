package com.phrasenest.expression.category.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite primary key for the expression_categories table.
 *
 * The combination of:
 * - expressionId
 * - categoryId
 *
 * must be unique.
 */
@Embeddable
public class ExpressionCategoryId implements Serializable {

    @Column(name = "expression_id")
    private UUID expressionId;

    @Column(name = "category_id")
    private UUID categoryId;

    /**
     * Required by JPA.
     */
    protected ExpressionCategoryId() {
    }

    public ExpressionCategoryId(
            UUID expressionId,
            UUID categoryId
    ) {
        this.expressionId = expressionId;
        this.categoryId = categoryId;
    }

    public UUID getExpressionId() {
        return expressionId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    /**
     * Composite key classes must implement equals() and hashCode().
     *
     * Hibernate uses these methods to compare entity identities.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ExpressionCategoryId that)) {
            return false;
        }

        return Objects.equals(
                expressionId,
                that.expressionId
        ) && Objects.equals(
                categoryId,
                that.categoryId
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                expressionId,
                categoryId
        );
    }
}
