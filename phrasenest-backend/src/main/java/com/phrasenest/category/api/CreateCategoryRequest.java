package com.phrasenest.category.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request used by an administrator to create a category.
 */
public record CreateCategoryRequest(

        /**
         * Category name displayed to the user.
         */
        @NotBlank(message = "Category name is required.")
        @Size(
                max = 100,
                message = "Category name cannot exceed 100 characters."
        )
        String name,

        /**
         * Optional category description.
         */
        @Size(
                max = 3000,
                message = "Description cannot exceed 3000 characters."
        )
        String description,

        /**
         * Optional parent category.
         *
         * A null value creates a top-level category.
         */
        UUID parentId,

        /**
         * Optional frontend icon identifier.
         */
        @Size(
                max = 50,
                message = "Icon name cannot exceed 50 characters."
        )
        String iconName,

        /**
         * Lower values are displayed first.
         */
        @Min(
                value = 0,
                message = "Display order cannot be negative."
        )
        @Max(
                value = 10000,
                message = "Display order is too large."
        )
        int displayOrder,

        /**
         * Determines whether the category is publicly visible.
         */
        boolean active
) {
}
