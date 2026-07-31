package com.phrasenest.expression.example.application;


import com.phrasenest.expression.domain.Expression;
import com.phrasenest.expression.example.api.*;
import com.phrasenest.expression.example.domain.ExpressionExample;
import com.phrasenest.expression.example.infrastructure.ExpressionExampleRepository;
import com.phrasenest.expression.infrastructure.ExpressionRepository;
import com.phrasenest.shared.exception.DuplicateResourceException;
import com.phrasenest.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Business logic for managing expression examples.
 */
@Service
@Transactional
public class ExpressionExampleService {

    private final ExpressionRepository expressionRepository;
    private final ExpressionExampleRepository exampleRepository;
    private final ExpressionExampleMapper exampleMapper;

    public ExpressionExampleService(
            ExpressionRepository expressionRepository,
            ExpressionExampleRepository exampleRepository,
            ExpressionExampleMapper exampleMapper
    ) {
        this.expressionRepository = expressionRepository;
        this.exampleRepository = exampleRepository;
        this.exampleMapper = exampleMapper;
    }

    public ExpressionExampleResponse create(
            UUID expressionId,
            CreateExpressionExampleRequest request
    ) {
        Expression expression = findExpression(expressionId);

        validateCreateDuplicate(
                expressionId,
                request.exampleText()
        );

        ExpressionExample example = new ExpressionExample(
                expression,
                request.exampleText().trim(),
                trimToNull(request.contextText()),
                request.exampleType(),
                request.difficultyLevel(),
                request.appropriateness(),
                trimToNull(request.explanation()),
                request.displayOrder()
        );

        ExpressionExample savedExample =
                exampleRepository.save(example);

        return exampleMapper.toResponse(savedExample);
    }

    @Transactional(readOnly = true)
    public List<ExpressionExampleResponse> getAllForExpression(
            UUID expressionId
    ) {
        findExpression(expressionId);

        return exampleRepository
                .findAllByExpressionIdOrderByDisplayOrderAscCreatedAtAsc(
                        expressionId
                )
                .stream()
                .map(exampleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExpressionExampleResponse getById(UUID exampleId) {
        return exampleMapper.toResponse(
                findExample(exampleId)
        );
    }

    public ExpressionExampleResponse update(
            UUID exampleId,
            UpdateExpressionExampleRequest request
    ) {
        ExpressionExample example = findExample(exampleId);

        UUID expressionId =
                example.getExpression().getId();

        boolean duplicateExists =
                exampleRepository
                        .existsByExpressionIdAndExampleTextIgnoreCaseAndIdNot(
                                expressionId,
                                request.exampleText().trim(),
                                exampleId
                        );

        if (duplicateExists) {
            throw new DuplicateResourceException(
                    "Another example already uses this sentence."
            );
        }

        example.update(
                request.exampleText().trim(),
                trimToNull(request.contextText()),
                request.exampleType(),
                request.difficultyLevel(),
                request.appropriateness(),
                trimToNull(request.explanation()),
                request.displayOrder()
        );

        return exampleMapper.toResponse(example);
    }

    public void delete(UUID exampleId) {
        ExpressionExample example = findExample(exampleId);
        exampleRepository.delete(example);
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

    private ExpressionExample findExample(UUID exampleId) {
        return exampleRepository
                .findById(exampleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Example with ID '" +
                                        exampleId +
                                        "' was not found."
                        )
                );
    }

    private void validateCreateDuplicate(
            UUID expressionId,
            String exampleText
    ) {
        boolean duplicateExists =
                exampleRepository
                        .existsByExpressionIdAndExampleTextIgnoreCase(
                                expressionId,
                                exampleText.trim()
                        );

        if (duplicateExists) {
            throw new DuplicateResourceException(
                    "This example already exists for the expression."
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