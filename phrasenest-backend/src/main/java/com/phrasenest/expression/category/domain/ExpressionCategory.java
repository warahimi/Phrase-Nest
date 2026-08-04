package com.phrasenest.expression.category.domain;

import com.phrasenest.category.domain.Category;
import com.phrasenest.expression.domain.Expression;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * Connects an expression to a category.
 *
 * We use an explicit entity instead of @ManyToMany because the relationship
 * itself contains useful information:
 *
 * - isPrimary
 * - assignedAt
 *
 * Additional metadata can be added later.
 */
@Entity
@Table(
        name = "expression_categories",
        indexes = {
                @Index(
                        name = "idx_expression_categories_expression_id",
                        columnList = "expression_id"
                ),
                @Index(
                        name = "idx_expression_categories_category_id",
                        columnList = "category_id"
                )
        }
)
public class ExpressionCategory {

    /**
     * Composite primary key.
     */
    @EmbeddedId
    private ExpressionCategoryId id;

    /**
     * Maps the expressionId field inside the composite key to this
     * Expression relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("expressionId")
    @JoinColumn(
            name = "expression_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_expression_categories_expression"
            )
    )
    private Expression expression;

    /**
     * Maps the categoryId field inside the composite key to this
     * Category relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("categoryId")
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_expression_categories_category"
            )
    )
    private Category category;

    /**
     * Marks the main category for an expression.
     *
     * Example:
     *
     * Expression:
     * "I second that."
     *
     * Primary category:
     * Agreement
     *
     * Additional categories:
     * Workplace
     * Meetings
     */
    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    /**
     * Time when the category was assigned.
     */
    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    /**
     * Required by JPA.
     */
    protected ExpressionCategory() {
    }

    public ExpressionCategory(
            Expression expression,
            Category category,
            boolean primary
    ) {
        this.id = new ExpressionCategoryId(
                expression.getId(),
                category.getId()
        );

        this.expression = expression;
        this.category = category;
        this.primary = primary;
        this.assignedAt = Instant.now();
    }

    /**
     * Changes whether this is the main category.
     */
    public void changePrimaryStatus(boolean primary) {
        this.primary = primary;
    }

    public ExpressionCategoryId getId() {
        return id;
    }

    public Expression getExpression() {
        return expression;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isPrimary() {
        return primary;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }
}
