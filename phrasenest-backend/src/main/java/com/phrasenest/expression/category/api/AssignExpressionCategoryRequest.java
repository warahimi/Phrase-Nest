package com.phrasenest.expression.category.api;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request used to assign a category to an expression.
 */
public record AssignExpressionCategoryRequest(

        @NotNull(message = "Category ID is required.")
        UUID categoryId,

        /**
         * True when this should be the expression's main category.
         */
        boolean primary
) {
}