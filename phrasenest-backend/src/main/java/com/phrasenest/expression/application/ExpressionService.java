package com.phrasenest.expression.application;


import com.phrasenest.expression.api.*;
import com.phrasenest.expression.domain.Expression;
import com.phrasenest.expression.domain.PublicationStatus;
import com.phrasenest.expression.infrastructure.ExpressionRepository;
import com.phrasenest.shared.exception.DuplicateResourceException;
import com.phrasenest.shared.exception.ResourceNotFoundException;
import com.phrasenest.shared.util.ExpressionNormalizer;
import com.phrasenest.shared.util.SlugGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Contains use-case and business logic for expressions.
 *
 * Controllers should remain thin. They receive HTTP requests and delegate
 * business work to this service.
 */
@Service
@Transactional
public class ExpressionService {

    private final ExpressionRepository expressionRepository;
    private final ExpressionNormalizer expressionNormalizer;
    private final SlugGenerator slugGenerator;
    private final ExpressionMapper expressionMapper;

    /**
     * Constructor injection makes dependencies explicit and testable.
     */
    public ExpressionService(
            ExpressionRepository expressionRepository,
            ExpressionNormalizer expressionNormalizer,
            SlugGenerator slugGenerator,
            ExpressionMapper expressionMapper
    ) {
        this.expressionRepository = expressionRepository;
        this.expressionNormalizer = expressionNormalizer;
        this.slugGenerator = slugGenerator;
        this.expressionMapper = expressionMapper;
    }

    /**
     * Creates a new expression.
     */
    public ExpressionResponse create(CreateExpressionRequest request) {

        String normalizedText =
                expressionNormalizer.normalize(request.canonicalText());

        String slug =
                slugGenerator.generate(request.canonicalText());

        validateCreateUniqueness(normalizedText, slug);

        Expression expression = new Expression(
                request.canonicalText().trim(),
                normalizedText,
                slug,
                request.expressionType(),
                request.shortMeaning().trim(),
                trimToNull(request.detailedMeaning()),
                trimToNull(request.literalMeaning()),
                trimToNull(request.usageNotes()),
                request.cefrLevel(),
                request.formality(),
                trimToNull(request.tone()),
                request.frequencyLevel(),
                trimToNull(request.region()),
                request.offensiveLevel(),
                request.publicationStatus(),
                request.sourceType()
        );

        Expression savedExpression =
                expressionRepository.save(expression);

        return expressionMapper.toResponse(savedExpression);
    }

    /**
     * Returns one expression by UUID.
     */
    @Transactional(readOnly = true)
    public ExpressionResponse getById(UUID id) {
        return expressionMapper.toResponse(findExpression(id));
    }

    /**
     * Returns one expression by slug for an administrative/internal view.
     */
    @Transactional(readOnly = true)
    public ExpressionResponse getBySlug(String slug) {
        Expression expression = expressionRepository
                .findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expression with slug '" + slug +
                                        "' was not found."
                        )
                );

        return expressionMapper.toResponse(expression);
    }

    /**
     * Returns only published expressions.
     *
     * This method will be used by the public browsing page.
     */
    @Transactional(readOnly = true)
    public Page<ExpressionResponse> getPublished(Pageable pageable) {
        return expressionRepository
                .findAllByPublicationStatus(
                        PublicationStatus.PUBLISHED,
                        pageable
                )
                .map(expressionMapper::toResponse);
    }

    /**
     * Updates the educational content of an expression.
     */
    public ExpressionResponse update(
            UUID id,
            UpdateExpressionRequest request
    ) {
        Expression expression = findExpression(id);

        String normalizedText =
                expressionNormalizer.normalize(request.canonicalText());

        String slug =
                slugGenerator.generate(request.canonicalText());

        validateUpdateUniqueness(id, normalizedText, slug);

        expression.updateContent(
                request.canonicalText().trim(),
                normalizedText,
                slug,
                request.expressionType(),
                request.shortMeaning().trim(),
                trimToNull(request.detailedMeaning()),
                trimToNull(request.literalMeaning()),
                trimToNull(request.usageNotes()),
                request.cefrLevel(),
                request.formality(),
                trimToNull(request.tone()),
                request.frequencyLevel(),
                trimToNull(request.region()),
                request.offensiveLevel()
        );

        /*
         * Because this method is transactional and the entity is managed by
         * Hibernate, calling save() is not strictly required.
         *
         * Hibernate dirty checking detects the changed fields and writes an
         * UPDATE when the transaction commits.
         */
        return expressionMapper.toResponse(expression);
    }

    /**
     * Changes an expression's publication workflow status.
     */
    public ExpressionResponse changePublicationStatus(
            UUID id,
            ChangePublicationStatusRequest request
    ) {
        Expression expression = findExpression(id);

        expression.changePublicationStatus(
                request.publicationStatus()
        );

        return expressionMapper.toResponse(expression);
    }

    /**
     * Permanently deletes an expression.
     *
     * Later, we may prefer ARCHIVED over permanent deletion for expressions
     * that already have user favorites or learning history.
     */
    public void delete(UUID id) {
        Expression expression = findExpression(id);
        expressionRepository.delete(expression);
    }

    private Expression findExpression(UUID id) {
        return expressionRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expression with ID '" + id +
                                        "' was not found."
                        )
                );
    }

    private void validateCreateUniqueness(
            String normalizedText,
            String slug
    ) {
        if (expressionRepository
                .existsByNormalizedText(normalizedText)) {

            throw new DuplicateResourceException(
                    "This expression already exists."
            );
        }

        if (expressionRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException(
                    "An expression with this URL slug already exists."
            );
        }
    }

    private void validateUpdateUniqueness(
            UUID id,
            String normalizedText,
            String slug
    ) {
        if (expressionRepository
                .existsByNormalizedTextAndIdNot(
                        normalizedText,
                        id
                )) {

            throw new DuplicateResourceException(
                    "Another expression already uses this text."
            );
        }

        if (expressionRepository.existsBySlugAndIdNot(slug, id)) {
            throw new DuplicateResourceException(
                    "Another expression already uses this URL slug."
            );
        }
    }

    /**
     * Converts empty optional strings into null.
     *
     * This keeps the database cleaner than storing many empty strings.
     */
    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    @Transactional(readOnly = true)
    public ExpressionResponse getPublishedBySlug(String slug) {
        Expression expression = expressionRepository
                .findBySlugAndPublicationStatus(
                        slug,
                        PublicationStatus.PUBLISHED
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Published expression with slug '" +
                                        slug + "' was not found."
                        )
                );

        return expressionMapper.toResponse(expression);
    }
}