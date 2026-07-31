package com.phrasenest.expression.alias.domain;


import com.phrasenest.expression.domain.Expression;
import com.phrasenest.shared.domain.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents an alternative searchable form of an expression.
 *
 * Example:
 *
 * Canonical expression:
 * "I wasn't born yesterday."
 *
 * Possible aliases:
 * - "I was not born yesterday."
 * - "wasn't born yesterday"
 * - "I wasnt born yesterday"
 *
 * Why use ManyToOne?
 *
 * Many aliases can point to one expression:
 *
 * Expression
 *     ├── Alias 1
 *     ├── Alias 2
 *     ├── Alias 3
 *     └── Alias 4
 *
 * Database relationship:
 *
 * expressions.id
 *       ↑
 * expression_aliases.expression_id
 */
@Entity
@Table(
        name = "expression_aliases",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_expression_aliases_normalized_alias",
                        columnNames = "normalized_alias"
                )
        },
        indexes = {
                @Index(
                        name = "idx_expression_aliases_expression_id",
                        columnList = "expression_id"
                ),
                @Index(
                        name = "idx_expression_aliases_alias_type",
                        columnList = "alias_type"
                )
        }
)
public class ExpressionAlias extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The expression to which this alias belongs.
     *
     * Many aliases can belong to one expression.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "expression_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_expression_aliases_expression"
            )
    )
    private Expression expression;

    /**
     * The alias as it should be displayed.
     *
     * Example:
     * "I was not born yesterday."
     */
    @Column(name = "alias_text", nullable = false, length = 500)
    private String aliasText;

    /**
     * Search-friendly version of aliasText.
     *
     * Example:
     * "i was not born yesterday"
     */
    @Column(name = "normalized_alias", nullable = false, length = 500)
    private String normalizedAlias;

    @Enumerated(EnumType.STRING)
    @Column(name = "alias_type", nullable = false, length = 40)
    private AliasType aliasType;

    /**
     * True means this alias is a correct form learners may use.
     *
     * False means the alias exists only to recognize a mistake or
     * misspelling and redirect the user to the correct expression.
     */
    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    /**
     * Optional explanation shown to the learner.
     *
     * Example:
     * "This spelling is commonly entered without an apostrophe,
     * but the correct form is 'wasn't'."
     */
    @Column(name = "usage_note", columnDefinition = "TEXT")
    private String usageNote;

    /**
     * Required by JPA.
     */
    protected ExpressionAlias() {
    }

    public ExpressionAlias(
            Expression expression,
            String aliasText,
            String normalizedAlias,
            AliasType aliasType,
            boolean correct,
            String usageNote
    ) {
        this.expression = expression;
        this.aliasText = aliasText;
        this.normalizedAlias = normalizedAlias;
        this.aliasType = aliasType;
        this.correct = correct;
        this.usageNote = usageNote;
    }

    /**
     * Updates the editable fields of the alias.
     */
    public void update(
            String aliasText,
            String normalizedAlias,
            AliasType aliasType,
            boolean correct,
            String usageNote
    ) {
        this.aliasText = aliasText;
        this.normalizedAlias = normalizedAlias;
        this.aliasType = aliasType;
        this.correct = correct;
        this.usageNote = usageNote;
    }

    public UUID getId() {
        return id;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getAliasText() {
        return aliasText;
    }

    public String getNormalizedAlias() {
        return normalizedAlias;
    }

    public AliasType getAliasType() {
        return aliasType;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getUsageNote() {
        return usageNote;
    }
}
