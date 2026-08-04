package com.phrasenest.category.application;


import com.phrasenest.category.api.CategoryResponse;
import com.phrasenest.category.api.CategoryTreeResponse;
import com.phrasenest.category.api.CreateCategoryRequest;
import com.phrasenest.category.api.UpdateCategoryRequest;
import com.phrasenest.category.domain.Category;
import com.phrasenest.category.infrastructure.CategoryRepository;
import com.phrasenest.expression.category.infrastructure.ExpressionCategoryRepository;
import com.phrasenest.shared.exception.DuplicateResourceException;
import com.phrasenest.shared.exception.ResourceNotFoundException;
import com.phrasenest.shared.util.SlugGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Contains category business logic.
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ExpressionCategoryRepository expressionCategoryRepository;
    private final CategoryMapper categoryMapper;
    private final SlugGenerator slugGenerator;

    public CategoryService(
            CategoryRepository categoryRepository,
            ExpressionCategoryRepository expressionCategoryRepository,
            CategoryMapper categoryMapper,
            SlugGenerator slugGenerator
    ) {
        this.categoryRepository = categoryRepository;
        this.expressionCategoryRepository = expressionCategoryRepository;
        this.categoryMapper = categoryMapper;
        this.slugGenerator = slugGenerator;
    }

    /**
     * Creates a category.
     */
    public CategoryResponse create(CreateCategoryRequest request) {

        String cleanName = request.name().trim();
        String slug = slugGenerator.generate(cleanName);

        validateCreateUniqueness(cleanName, slug);

        /*
         * If parentId is null, the new category is a top-level category.
         */
        Category parent = resolveOptionalParent(request.parentId());

        Category category = new Category(
                cleanName,
                slug,
                trimToNull(request.description()),
                parent,
                trimToNull(request.iconName()),
                request.displayOrder(),
                request.active()
        );

        return categoryMapper.toResponse(
                categoryRepository.save(category)
        );
    }

    /**
     * Returns all categories for administrators.
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        return categoryRepository
                .findAllByOrderByDisplayOrderAscNameAsc()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    /**
     * Returns one category by ID.
     */
    @Transactional(readOnly = true)
    public CategoryResponse getById(UUID categoryId) {
        return categoryMapper.toResponse(
                findCategory(categoryId)
        );
    }

    /**
     * Returns an active category by slug for public usage.
     */
    @Transactional(readOnly = true)
    public CategoryResponse getActiveBySlug(String slug) {
        Category category = categoryRepository
                .findBySlugAndActiveTrue(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active category with slug '" +
                                        slug +
                                        "' was not found."
                        )
                );

        return categoryMapper.toResponse(category);
    }

    /**
     * Builds a tree of active root categories and their active children.
     */
    @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getActiveCategoryTree() {

        List<Category> rootCategories = categoryRepository
                .findAllByParentIsNullAndActiveTrueOrderByDisplayOrderAscNameAsc();

        return rootCategories
                .stream()
                .map(this::buildTreeNode)
                .toList();
    }

    /**
     * Updates an existing category.
     */
    public CategoryResponse update(
            UUID categoryId,
            UpdateCategoryRequest request
    ) {
        Category category = findCategory(categoryId);

        String cleanName = request.name().trim();
        String slug = slugGenerator.generate(cleanName);

        validateUpdateUniqueness(
                categoryId,
                cleanName,
                slug
        );

        Category parent = resolveOptionalParent(request.parentId());

        /*
         * Prevent a category from being its own parent.
         */
        if (parent != null &&
                parent.getId().equals(categoryId)) {

            throw new IllegalArgumentException(
                    "A category cannot be its own parent."
            );
        }

        /*
         * Prevent a simple circular hierarchy:
         *
         * Parent category:
         * Workplace
         *
         * Child category:
         * Meetings
         *
         * We must not later make Workplace a child of Meetings.
         */
        validateNoCircularParent(category, parent);

        category.update(
                cleanName,
                slug,
                trimToNull(request.description()),
                parent,
                trimToNull(request.iconName()),
                request.displayOrder(),
                request.active()
        );

        /*
         * Hibernate dirty checking writes the changes when the transaction
         * commits.
         */
        return categoryMapper.toResponse(category);
    }

    /**
     * Deletes a category only when it has no child categories and is not
     * assigned to any expression.
     */
    public void delete(UUID categoryId) {
        Category category = findCategory(categoryId);

        if (categoryRepository.existsByParentId(categoryId)) {
            throw new IllegalStateException(
                    "The category cannot be deleted because it has child categories."
            );
        }

        if (expressionCategoryRepository.existsByCategoryId(categoryId)) {
            throw new IllegalStateException(
                    "The category cannot be deleted because expressions are assigned to it."
            );
        }

        categoryRepository.delete(category);
    }

    /**
     * Converts one entity into a tree node.
     */
    private CategoryTreeResponse buildTreeNode(Category category) {

        List<CategoryTreeResponse> children = categoryRepository
                .findAllByParentIdAndActiveTrueOrderByDisplayOrderAscNameAsc(
                        category.getId()
                )
                .stream()
                .map(this::buildTreeNode)
                .toList();

        return new CategoryTreeResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getIconName(),
                category.getDisplayOrder(),
                children
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

    /**
     * Returns null for top-level categories.
     */
    private Category resolveOptionalParent(UUID parentId) {
        if (parentId == null) {
            return null;
        }

        return findCategory(parentId);
    }

    private void validateCreateUniqueness(
            String name,
            String slug
    ) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException(
                    "A category with this name already exists."
            );
        }

        if (categoryRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException(
                    "A category with this URL slug already exists."
            );
        }
    }

    private void validateUpdateUniqueness(
            UUID categoryId,
            String name,
            String slug
    ) {
        if (categoryRepository
                .existsByNameIgnoreCaseAndIdNot(
                        name,
                        categoryId
                )) {

            throw new DuplicateResourceException(
                    "Another category already uses this name."
            );
        }

        if (categoryRepository.existsBySlugAndIdNot(
                slug,
                categoryId
        )) {
            throw new DuplicateResourceException(
                    "Another category already uses this URL slug."
            );
        }
    }

    /**
     * Traverses upward from the proposed parent.
     *
     * If the category being updated appears anywhere in that parent chain,
     * assigning the parent would create a circular hierarchy.
     */
    private void validateNoCircularParent(
            Category category,
            Category proposedParent
    ) {
        Category current = proposedParent;

        while (current != null) {
            if (current.getId().equals(category.getId())) {
                throw new IllegalArgumentException(
                        "The selected parent would create a circular category hierarchy."
                );
            }

            current = current.getParent();
        }
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}