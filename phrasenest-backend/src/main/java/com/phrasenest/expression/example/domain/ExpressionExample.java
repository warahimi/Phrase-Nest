package com.phrasenest.expression.example.domain;


import com.phrasenest.expression.domain.Expression;
import com.phrasenest.shared.domain.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents one sentence showing how an expression is used.
 */
@Entity
@Table(
        name = "expression_examples",
        indexes = {
                @Index(
                        name = "idx_expression_examples_expression_id",
                        columnList = "expression_id"
                ),
                @Index(
                        name = "idx_expression_examples_display_order",
                        columnList = "expression_id, display_order"
                )
        }
)
public class ExpressionExample extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Many examples can belong to one expression.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "expression_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_expression_examples_expression"
            )
    )
    private Expression expression;

    /**
     * The actual example sentence.
     */
    @Column(name = "example_text", nullable = false, columnDefinition = "TEXT")
    private String exampleText;

    /**
     * Explains the situation in which the sentence is being used.
     *
     * Example:
     * "A coworker agrees with a proposal during a meeting."
     */
    @Column(name = "context_text", columnDefinition = "TEXT")
    private String contextText;

    @Enumerated(EnumType.STRING)
    @Column(name = "example_type", nullable = false, length = 40)
    private ExampleType exampleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", length = 40)
    private DifficultyLevel difficultyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "appropriateness", nullable = false, length = 40)
    private Appropriateness appropriateness;

    /**
     * Optional educational explanation.
     *
     * This is especially useful for incorrect or cautious examples.
     */
    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    /**
     * Controls the order in which examples appear.
     *
     * Smaller values appear first.
     */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /**
     * Required by JPA.
     */
    protected ExpressionExample() {
    }

    public ExpressionExample(
            Expression expression,
            String exampleText,
            String contextText,
            ExampleType exampleType,
            DifficultyLevel difficultyLevel,
            Appropriateness appropriateness,
            String explanation,
            int displayOrder
    ) {
        this.expression = expression;
        this.exampleText = exampleText;
        this.contextText = contextText;
        this.exampleType = exampleType;
        this.difficultyLevel = difficultyLevel;
        this.appropriateness = appropriateness;
        this.explanation = explanation;
        this.displayOrder = displayOrder;
    }

    public void update(
            String exampleText,
            String contextText,
            ExampleType exampleType,
            DifficultyLevel difficultyLevel,
            Appropriateness appropriateness,
            String explanation,
            int displayOrder
    ) {
        this.exampleText = exampleText;
        this.contextText = contextText;
        this.exampleType = exampleType;
        this.difficultyLevel = difficultyLevel;
        this.appropriateness = appropriateness;
        this.explanation = explanation;
        this.displayOrder = displayOrder;
    }

    public UUID getId() {
        return id;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getExampleText() {
        return exampleText;
    }

    public String getContextText() {
        return contextText;
    }

    public ExampleType getExampleType() {
        return exampleType;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public Appropriateness getAppropriateness() {
        return appropriateness;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}