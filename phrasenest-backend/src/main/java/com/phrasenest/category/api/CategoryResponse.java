package com.phrasenest.category.api;


import java.time.Instant;
import java.util.UUID;

/**
 * Category information returned to the frontend.
 */
public record CategoryResponse(

        UUID id,

        String name,

        String slug,

        String description,

        /**
         * Null when this is a top-level category.
         */
        UUID parentId,

        /**
         * Null when this is a top-level category.
         */
        String parentName,

        String iconName,

        int displayOrder,

        boolean active,

        Instant createdAt,

        Instant updatedAt
) {
}
