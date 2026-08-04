package com.phrasenest.category.infrastructure;


import com.phrasenest.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Database access for categories.
 *
 * Spring Data JPA creates the implementation automatically.
 */
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * Finds a category by its URL slug.
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Finds an active category by slug.
     *
     * This method is used by public endpoints.
     */
    Optional<Category> findBySlugAndActiveTrue(String slug);

    /**
     * Checks whether a category name already exists, ignoring case.
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Checks whether a slug already exists.
     */
    boolean existsBySlug(String slug);

    /**
     * Used when updating a category.
     *
     * The current category's ID is excluded from duplicate checking.
     */
    boolean existsByNameIgnoreCaseAndIdNot(
            String name,
            UUID categoryId
    );

    /**
     * Used when updating a category slug.
     */
    boolean existsBySlugAndIdNot(
            String slug,
            UUID categoryId
    );

    /**
     * Returns active top-level categories.
     *
     * A top-level category has no parent.
     */
    List<Category>
    findAllByParentIsNullAndActiveTrueOrderByDisplayOrderAscNameAsc();

    /**
     * Returns all active child categories of one parent.
     */
    List<Category>
    findAllByParentIdAndActiveTrueOrderByDisplayOrderAscNameAsc(
            UUID parentId
    );

    /**
     * Returns all categories for the administration interface.
     */
    List<Category> findAllByOrderByDisplayOrderAscNameAsc();

    /**
     * Checks whether a category has child categories.
     *
     * This is useful before deletion.
     */
    boolean existsByParentId(UUID parentId);
}
