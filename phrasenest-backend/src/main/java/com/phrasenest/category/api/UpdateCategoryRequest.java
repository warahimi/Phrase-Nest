package com.phrasenest.category.api;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request used to update an existing category.
 */
public record UpdateCategoryRequest(

        @NotBlank(message = "Category name is required.")
        @Size(max = 100)
        String name,

        @Size(max = 3000)
        String description,

        UUID parentId,

        @Size(max = 50)
        String iconName,

        @Min(0)
        @Max(10000)
        int displayOrder,

        boolean active
) {
}
