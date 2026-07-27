package com.phrasenest.expression.domain;

import com.phrasenest.shared.domain.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents one idiom, phrasal verb, proverb, or common expression.
 *
 * Examples:
 * - "I wasn't born yesterday."
 * - "I second that."
 * - "Give up."
 * - "Better late than never."
 */
@Entity
@Table(
        name = "expressions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_expressions_normalized_text",
                        columnNames = "normalized_text"
                ),
                @UniqueConstraint(
                        name = "uk_expressions_slug",
                        columnNames = "slug"
                )
        },
        indexes = {
                @Index(
                        name = "idx_expressions_type",
                        columnList = "expression_type"
                ),
                @Index(
                        name = "idx_expressions_publication_status",
                        columnList = "publication_status"
                ),
                @Index(
                        name = "idx_expressions_created_at",
                        columnList = "created_at"
                )
        }
)
public class Expression extends BaseEntity {

    /**
     * UUID is suitable because:
     * - IDs are difficult to guess.
     * - Records can later be generated across multiple services.
     * - Mobile and distributed systems can create IDs safely.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Correct form displayed to users.
     *
     * Example:
     * "I wasn't born yesterday."
     */
    @Column(name = "canonical_text", nullable = false, length = 500)
    private String canonicalText;

    /**
     * Search-friendly version of canonicalText.
     *
     * Example:
     * canonicalText  = "I wasn't born yesterday."
     * normalizedText = "i wasn't born yesterday"
     *
     * The application generates this value.
     */
    @Column(name = "normalized_text", nullable = false, length = 500)
    private String normalizedText;

    /**
     * URL-friendly value.
     *
     * Example:
     * /expressions/i-wasnt-born-yesterday
     */
    @Column(name = "slug", nullable = false, length = 550)
    private String slug;

    /**
     * Stored as text instead of an enum number.
     *
     * Good:
     * IDIOM
     *
     * Avoid:
     * 0
     *
     * EnumType.STRING keeps database values understandable and prevents
     * problems when enum order changes.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "expression_type", nullable = false, length = 50)
    private ExpressionType expressionType;

    /**
     * A short learner-friendly definition.
     *
     * Example:
     * "I am not naive or easily fooled."
     */
    @Column(name = "short_meaning", nullable = false, columnDefinition = "TEXT")
    private String shortMeaning;

    /**
     * A more complete explanation of the expression.
     */
    @Column(name = "detailed_meaning", columnDefinition = "TEXT")
    private String detailedMeaning;

    /**
     * The direct or word-for-word meaning.
     *
     * This is useful when the idiomatic meaning is different from the
     * literal meaning.
     */
    @Column(name = "literal_meaning", columnDefinition = "TEXT")
    private String literalMeaning;

    /**
     * Explains when, where, and how the expression should be used.
     */
    @Column(name = "usage_notes", columnDefinition = "TEXT")
    private String usageNotes;

    /**
     * Approximate learner level:
     * A1, A2, B1, B2, C1, or C2.
     */
    @Column(name = "cefr_level", length = 5)
    private String cefrLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "formality", length = 30)
    private Formality formality;

    /**
     * Free-text tone for now.
     *
     * Examples:
     * - FRIENDLY
     * - HUMOROUS
     * - ASSERTIVE
     * - SARCASTIC
     *
     * We may later move tone into a many-to-many table because one
     * expression can have multiple tones.
     */
    @Column(name = "tone", length = 100)
    private String tone;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_level", length = 30)
    private FrequencyLevel frequencyLevel;

    /**
     * Examples:
     * - WIDELY_USED
     * - AMERICAN_ENGLISH
     * - BRITISH_ENGLISH
     */
    @Column(name = "region", length = 50)
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(name = "offensive_level", nullable = false, length = 30)
    private OffensiveLevel offensiveLevel = OffensiveLevel.NONE;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = false, length = 30)
    private PublicationStatus publicationStatus = PublicationStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 30)
    private SourceType sourceType = SourceType.EDITORIAL;

    /**
     * Confidence value between 0 and 1.
     *
     * Mainly useful for AI-generated records.
     */
    @Column(name = "confidence_score")
    private Double confidenceScore;

    /**
     * Indicates whether administrators selected the expression for
     * special display on the home page.
     */
    @Column(name = "is_featured", nullable = false)
    private boolean featured = false;

    /**
     * Denormalized counters for fast display.
     *
     * We will later update these through the popularity module.
     */
    @Column(name = "view_count", nullable = false)
    private long viewCount = 0;

    @Column(name = "search_count", nullable = false)
    private long searchCount = 0;

    @Column(name = "save_count", nullable = false)
    private long saveCount = 0;

    /**
     * JPA requires a no-argument constructor.
     *
     * protected prevents normal application code from creating an
     * incomplete Expression accidentally.
     */
    protected Expression() {
    }

    /**
     * Main constructor used by our service when creating an expression.
     */
    public Expression(
            String canonicalText,
            String normalizedText,
            String slug,
            ExpressionType expressionType,
            String shortMeaning,
            String detailedMeaning,
            String literalMeaning,
            String usageNotes,
            String cefrLevel,
            Formality formality,
            String tone,
            FrequencyLevel frequencyLevel,
            String region,
            OffensiveLevel offensiveLevel,
            PublicationStatus publicationStatus,
            SourceType sourceType
    ) {
        this.canonicalText = canonicalText;
        this.normalizedText = normalizedText;
        this.slug = slug;
        this.expressionType = expressionType;
        this.shortMeaning = shortMeaning;
        this.detailedMeaning = detailedMeaning;
        this.literalMeaning = literalMeaning;
        this.usageNotes = usageNotes;
        this.cefrLevel = cefrLevel;
        this.formality = formality;
        this.tone = tone;
        this.frequencyLevel = frequencyLevel;
        this.region = region;
        this.offensiveLevel = offensiveLevel;
        this.publicationStatus = publicationStatus;
        this.sourceType = sourceType;
    }

    /**
     * Updates the editable content of an existing expression.
     *
     * Keeping updates inside the entity prevents the service from directly
     * changing every field separately.
     */
    public void updateContent(
            String canonicalText,
            String normalizedText,
            String slug,
            ExpressionType expressionType,
            String shortMeaning,
            String detailedMeaning,
            String literalMeaning,
            String usageNotes,
            String cefrLevel,
            Formality formality,
            String tone,
            FrequencyLevel frequencyLevel,
            String region,
            OffensiveLevel offensiveLevel
    ) {
        this.canonicalText = canonicalText;
        this.normalizedText = normalizedText;
        this.slug = slug;
        this.expressionType = expressionType;
        this.shortMeaning = shortMeaning;
        this.detailedMeaning = detailedMeaning;
        this.literalMeaning = literalMeaning;
        this.usageNotes = usageNotes;
        this.cefrLevel = cefrLevel;
        this.formality = formality;
        this.tone = tone;
        this.frequencyLevel = frequencyLevel;
        this.region = region;
        this.offensiveLevel = offensiveLevel;
    }

    /**
     * Changes the editorial status separately from ordinary content editing.
     */
    public void changePublicationStatus(PublicationStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public void markAsFeatured() {
        this.featured = true;
    }

    public void removeFromFeatured() {
        this.featured = false;
    }

    public UUID getId() {
        return id;
    }

    public String getCanonicalText() {
        return canonicalText;
    }

    public String getNormalizedText() {
        return normalizedText;
    }

    public String getSlug() {
        return slug;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public String getShortMeaning() {
        return shortMeaning;
    }

    public String getDetailedMeaning() {
        return detailedMeaning;
    }

    public String getLiteralMeaning() {
        return literalMeaning;
    }

    public String getUsageNotes() {
        return usageNotes;
    }

    public String getCefrLevel() {
        return cefrLevel;
    }

    public Formality getFormality() {
        return formality;
    }

    public String getTone() {
        return tone;
    }

    public FrequencyLevel getFrequencyLevel() {
        return frequencyLevel;
    }

    public String getRegion() {
        return region;
    }

    public OffensiveLevel getOffensiveLevel() {
        return offensiveLevel;
    }

    public PublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public boolean isFeatured() {
        return featured;
    }

    public long getViewCount() {
        return viewCount;
    }

    public long getSearchCount() {
        return searchCount;
    }

    public long getSaveCount() {
        return saveCount;
    }
}
