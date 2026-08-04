package com.phrasenest.expression.category.application;

import com.phrasenest.category.api.CategoryResponse;
import com.phrasenest.category.application.CategoryMapper;
import com.phrasenest.category.domain.Category;
import com.phrasenest.category.infrastructure.CategoryRepository;
import com.phrasenest.expression.api.ExpressionSummaryResponse;
import com.phrasenest.expression.category.api.AssignExpressionCategoryRequest;
import com.phrasenest.expression.category.api.CategoryExpressionsResponse;
import com.phrasenest.expression.category.api.ExpressionCategoryResponse;
import com.phrasenest.expression.category.domain.ExpressionCategory;
import com.phrasenest.expression.category.infrastructure.ExpressionCategoryRepository;
import com.phrasenest.expression.domain.Expression;
import com.phrasenest.expression.infrastructure.ExpressionRepository;
import com.phrasenest.shared.exception.DuplicateResourceException;
import com.phrasenest.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Business logic for assigning categories to expressions.
 */
@Service
@Transactional
public class ExpressionCategoryService {

    private final ExpressionCategoryRepository assignmentRepository;
    private final ExpressionRepository expressionRepository;
    private final CategoryRepository categoryRepository;
    private final ExpressionCategoryMapper assignmentMapper;
    private final CategoryMapper categoryMapper;

    public ExpressionCategoryService(
            ExpressionCategoryRepository assignmentRepository,
            ExpressionRepository expressionRepository,
            CategoryRepository categoryRepository,
            ExpressionCategoryMapper assignmentMapper,
            CategoryMapper categoryMapper
    ) {
        this.assignmentRepository = assignmentRepository;
        this.expressionRepository = expressionRepository;
        this.categoryRepository = categoryRepository;
        this.assignmentMapper = assignmentMapper;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Assigns one category to one expression.
     */
    public ExpressionCategoryResponse assign(
            UUID expressionId,
            AssignExpressionCategoryRequest request
    ) {
        Expression expression = findExpression(expressionId);
        Category category = findCategory(request.categoryId());

        if (assignmentRepository
                .existsByExpressionIdAndCategoryId(
                        expressionId,
                        category.getId()
                )) {

            throw new DuplicateResourceException(
                    "This category is already assigned to the expression."
            );
        }

        /*
         * If the new assignment is primary, remove the primary flag from
         * the old primary category first.
         */
        if (request.primary()) {
            clearExistingPrimaryCategory(expressionId);
        }

        ExpressionCategory assignment =
                new ExpressionCategory(
                        expression,
                        category,
                        request.primary()
                );

        return assignmentMapper.toResponse(
                assignmentRepository.save(assignment)
        );
    }

    /**
     * Returns all category assignments for an expression.
     */
    @Transactional(readOnly = true)
    public List<ExpressionCategoryResponse>
    getCategoriesForExpression(UUID expressionId) {

        findExpression(expressionId);

        return assignmentRepository
                .findAllWithCategoryByExpressionId(expressionId)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
    }

    /**
     * Marks one existing assignment as the primary category.
     */
    public ExpressionCategoryResponse makePrimary(
            UUID expressionId,
            UUID categoryId
    ) {
        ExpressionCategory assignment =
                findAssignment(expressionId, categoryId);

        clearExistingPrimaryCategory(expressionId);

        assignment.changePrimaryStatus(true);

        return assignmentMapper.toResponse(assignment);
    }

    /**
     * Removes a category from an expression.
     */
    public void remove(
            UUID expressionId,
            UUID categoryId
    ) {
        ExpressionCategory assignment =
                findAssignment(expressionId, categoryId);

        assignmentRepository.delete(assignment);
    }

    /**
     * Returns a public category page with its published expressions.
     */
    @Transactional(readOnly = true)
    public CategoryExpressionsResponse
    getPublishedExpressionsByCategorySlug(String categorySlug) {

        Category category = categoryRepository
                .findBySlugAndActiveTrue(categorySlug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active category with slug '" +
                                        categorySlug +
                                        "' was not found."
                        )
                );

        List<ExpressionSummaryResponse> expressions =
                assignmentRepository
                        .findAllPublishedWithExpressionByCategoryId(
                                category.getId()
                        )
                        .stream()
                        .map(ExpressionCategory::getExpression)
                        .map(assignmentMapper::toExpressionSummary)
                        .toList();

        CategoryResponse categoryResponse =
                categoryMapper.toResponse(category);

        return new CategoryExpressionsResponse(
                categoryResponse,
                expressions
        );
    }

    /**
     * Removes the primary flag from the existing primary assignment.
     */
    private void clearExistingPrimaryCategory(
            UUID expressionId
    ) {
        assignmentRepository
                .findByExpressionIdAndPrimaryTrue(expressionId)
                .ifPresent(existingPrimary ->
                        existingPrimary.changePrimaryStatus(false)
                );
    }

    private ExpressionCategory findAssignment(
            UUID expressionId,
            UUID categoryId
    ) {
        return assignmentRepository
                .findByExpressionIdAndCategoryId(
                        expressionId,
                        categoryId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "The expression-category assignment was not found."
                        )
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

    private Category findCategory(UUID categoryId) {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category with ID '" +
                                        categoryId +
                                        "' was not found."
                        )
                );
    }
}