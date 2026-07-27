package com.phrasenest.expression.infrastructure;

import com.phrasenest.expression.domain.Expression;
import com.phrasenest.expression.domain.PublicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data creates the implementation of this interface automatically.
 *
 * JpaRepository gives us:
 * - save()
 * - findById()
 * - findAll()
 * - delete()
 * - count()
 * - pagination
 * - sorting
 */
public interface ExpressionRepository
        extends JpaRepository<Expression, UUID> {

    /**
     * Finds an expression using its URL slug.
     */
    Optional<Expression> findBySlug(String slug);

    /**
     * Finds a published expression using its slug.
     *
     * This method will later be used by the public expression detail page.
     */
    Optional<Expression> findBySlugAndPublicationStatus(
            String slug,
            PublicationStatus publicationStatus
    );

    /**
     * Finds an expression after the user's query has been normalized.
     */
    Optional<Expression> findByNormalizedText(String normalizedText);

    /**
     * Checks for duplicate normalized text before insertion.
     */
    boolean existsByNormalizedText(String normalizedText);

    /**
     * Checks whether a slug is already being used.
     */
    boolean existsBySlug(String slug);

    /**
     * Used during updates so the current record does not count as a duplicate.
     */
    boolean existsByNormalizedTextAndIdNot(
            String normalizedText,
            UUID id
    );

    /**
     * Used during updates to verify slug uniqueness.
     */
    boolean existsBySlugAndIdNot(
            String slug,
            UUID id
    );

    /**
     * Returns published expressions with pagination.
     */
    Page<Expression> findAllByPublicationStatus(
            PublicationStatus publicationStatus,
            Pageable pageable
    );
}