package com.phrasenest.expression.dialogue.domain;


import com.phrasenest.expression.domain.Expression;
import com.phrasenest.expression.example.domain.DifficultyLevel;
import com.phrasenest.shared.domain.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents a short two-speaker conversation using an expression.
 */
@Entity
@Table(
        name = "expression_dialogues",
        indexes = {
                @Index(
                        name = "idx_expression_dialogues_expression_id",
                        columnList = "expression_id"
                ),
                @Index(
                        name = "idx_expression_dialogues_display_order",
                        columnList = "expression_id, display_order"
                )
        }
)
public class ExpressionDialogue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "expression_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_expression_dialogues_expression"
            )
    )
    private Expression expression;

    /**
     * Optional speaker name or label.
     *
     * Examples:
     * "Manager", "Student", "A", or "Maria"
     */
    @Column(name = "speaker_a_name", length = 100)
    private String speakerAName;

    @Column(name = "speaker_a_text", nullable = false, columnDefinition = "TEXT")
    private String speakerAText;

    @Column(name = "speaker_b_name", length = 100)
    private String speakerBName;

    @Column(name = "speaker_b_text", nullable = false, columnDefinition = "TEXT")
    private String speakerBText;

    /**
     * Explains where or why the conversation occurs.
     */
    @Column(name = "context_text", columnDefinition = "TEXT")
    private String contextText;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", length = 40)
    private DifficultyLevel difficultyLevel;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    protected ExpressionDialogue() {
    }

    public ExpressionDialogue(
            Expression expression,
            String speakerAName,
            String speakerAText,
            String speakerBName,
            String speakerBText,
            String contextText,
            DifficultyLevel difficultyLevel,
            int displayOrder
    ) {
        this.expression = expression;
        this.speakerAName = speakerAName;
        this.speakerAText = speakerAText;
        this.speakerBName = speakerBName;
        this.speakerBText = speakerBText;
        this.contextText = contextText;
        this.difficultyLevel = difficultyLevel;
        this.displayOrder = displayOrder;
    }

    public void update(
            String speakerAName,
            String speakerAText,
            String speakerBName,
            String speakerBText,
            String contextText,
            DifficultyLevel difficultyLevel,
            int displayOrder
    ) {
        this.speakerAName = speakerAName;
        this.speakerAText = speakerAText;
        this.speakerBName = speakerBName;
        this.speakerBText = speakerBText;
        this.contextText = contextText;
        this.difficultyLevel = difficultyLevel;
        this.displayOrder = displayOrder;
    }

    public UUID getId() {
        return id;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getSpeakerAName() {
        return speakerAName;
    }

    public String getSpeakerAText() {
        return speakerAText;
    }

    public String getSpeakerBName() {
        return speakerBName;
    }

    public String getSpeakerBText() {
        return speakerBText;
    }

    public String getContextText() {
        return contextText;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
