package com.phrasenest.expression.alias.application;


import com.phrasenest.expression.alias.api.*;
import com.phrasenest.expression.alias.domain.ExpressionAlias;
import com.phrasenest.expression.alias.infrastructure.ExpressionAliasRepository;
import com.phrasenest.expression.api.ExpressionResponse;
import com.phrasenest.expression.application.ExpressionMapper;
import com.phrasenest.expression.domain.Expression;
import com.phrasenest.expression.domain.PublicationStatus;
import com.phrasenest.expression.infrastructure.ExpressionRepository;
import com.phrasenest.shared.exception.DuplicateResourceException;
import com.phrasenest.shared.exception.ResourceNotFoundException;
import com.phrasenest.shared.util.ExpressionNormalizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Contains business logic related to expression aliases.
 */
@Service
@Transactional
public class ExpressionAliasService {

    private final ExpressionAliasRepository aliasRepository;
    private final ExpressionRepository expressionRepository;
    private final ExpressionNormalizer expressionNormalizer;
    private final ExpressionAliasMapper aliasMapper;
    private final ExpressionMapper expressionMapper;

    public ExpressionAliasService(
            ExpressionAliasRepository aliasRepository,
            ExpressionRepository expressionRepository,
            ExpressionNormalizer expressionNormalizer,
            ExpressionAliasMapper aliasMapper,
            ExpressionMapper expressionMapper
    ) {
        this.aliasRepository = aliasRepository;
        this.expressionRepository = expressionRepository;
        this.expressionNormalizer = expressionNormalizer;
        this.aliasMapper = aliasMapper;
        this.expressionMapper = expressionMapper;
    }

    /**
     * Adds an alias to an existing expression.
     */
    public ExpressionAliasResponse create(
            UUID expressionId,
            CreateExpressionAliasRequest request
    ) {
        Expression expression = findExpression(expressionId);

        String normalizedAlias =
                expressionNormalizer.normalize(request.aliasText());

        validateAliasDoesNotMatchCanonicalText(
                expression,
                normalizedAlias
        );

        validateAliasUniqueness(normalizedAlias);

        ExpressionAlias alias = new ExpressionAlias(
                expression,
                request.aliasText().trim(),
                normalizedAlias,
                request.aliasType(),
                request.correct(),
                trimToNull(request.usageNote())
        );

        ExpressionAlias savedAlias =
                aliasRepository.save(alias);

        return aliasMapper.toResponse(savedAlias);
    }

    /**
     * Returns all aliases belonging to one expression.
     */
    @Transactional(readOnly = true)
    public List<ExpressionAliasResponse> getAllForExpression(
            UUID expressionId
    ) {
        /*
         * First confirm that the expression exists.
         *
         * Otherwise an invalid expression ID and an expression with no
         * aliases would both return an empty list.
         */
        findExpression(expressionId);

        return aliasRepository
                .findAllByExpressionIdOrderByCreatedAtAsc(
                        expressionId
                )
                .stream()
                .map(aliasMapper::toResponse)
                .toList();
    }

    /**
     * Returns one alias by ID.
     */
    @Transactional(readOnly = true)
    public ExpressionAliasResponse getById(UUID aliasId) {
        return aliasMapper.toResponse(
                findAlias(aliasId)
        );
    }

    /**
     * Updates an existing alias.
     */
    public ExpressionAliasResponse update(
            UUID aliasId,
            UpdateExpressionAliasRequest request
    ) {
        ExpressionAlias alias = findAlias(aliasId);

        String normalizedAlias =
                expressionNormalizer.normalize(request.aliasText());

        validateAliasDoesNotMatchCanonicalText(
                alias.getExpression(),
                normalizedAlias
        );

        if (aliasRepository
                .existsByNormalizedAliasAndIdNot(
                        normalizedAlias,
                        aliasId
                )) {

            throw new DuplicateResourceException(
                    "Another alias already uses this text."
            );
        }

        alias.update(
                request.aliasText().trim(),
                normalizedAlias,
                request.aliasType(),
                request.correct(),
                trimToNull(request.usageNote())
        );

        /*
         * Hibernate dirty checking will update the database when the
         * transaction commits.
         */
        return aliasMapper.toResponse(alias);
    }

    /**
     * Deletes one alias.
     */
    public void delete(UUID aliasId) {
        ExpressionAlias alias = findAlias(aliasId);
        aliasRepository.delete(alias);
    }

    /**
     * Resolves a user's query through the alias table.
     *
     * This method only returns expressions that are published.
     */
    @Transactional(readOnly = true)
    public AliasResolutionResponse resolvePublicAlias(
            String query
    ) {
        String normalizedQuery =
                expressionNormalizer.normalize(query);

        ExpressionAlias alias = aliasRepository
                .findWithExpressionByNormalizedAlias(
                        normalizedQuery
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No expression alias matched the query."
                        )
                );

        Expression expression = alias.getExpression();

        /*
         * Draft or rejected expressions must not be exposed through
         * a public alias lookup.
         */
        if (expression.getPublicationStatus()
                != PublicationStatus.PUBLISHED) {

            throw new ResourceNotFoundException(
                    "No published expression matched the query."
            );
        }

        ExpressionResponse expressionResponse =
                expressionMapper.toResponse(expression);

        return new AliasResolutionResponse(
                alias.getAliasText(),
                alias.isCorrect(),
                alias.getAliasType(),
                alias.getUsageNote(),
                expressionResponse
        );
    }

    private Expression findExpression(UUID expressionId) {
        return expressionRepository
                .findById(expressionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expression with ID '" +
                                        expressionId +
                                        "' was not found."
                        )
                );
    }

    private ExpressionAlias findAlias(UUID aliasId) {
        return aliasRepository
                .findById(aliasId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Alias with ID '" +
                                        aliasId +
                                        "' was not found."
                        )
                );
    }

    private void validateAliasUniqueness(
            String normalizedAlias
    ) {
        if (aliasRepository
                .existsByNormalizedAlias(normalizedAlias)) {

            throw new DuplicateResourceException(
                    "This alias already exists."
            );
        }
    }

    /**
     * Prevents storing the canonical expression as its own alias.
     *
     * Bad:
     *
     * Expression:
     * "I wasn't born yesterday."
     *
     * Alias:
     * "I wasn't born yesterday."
     */
    private void validateAliasDoesNotMatchCanonicalText(
            Expression expression,
            String normalizedAlias
    ) {
        if (expression.getNormalizedText()
                .equals(normalizedAlias)) {

            throw new DuplicateResourceException(
                    "The alias is the same as the canonical expression."
            );
        }
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}