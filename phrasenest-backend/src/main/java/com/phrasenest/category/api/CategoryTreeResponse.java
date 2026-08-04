package com.phrasenest.category.api;


import java.util.List;
import java.util.UUID;

/**
 * Represents one category and its direct children.
 *
 * The current implementation returns two levels:
 * - Parent
 * - Direct children
 *
 * We can later make this recursively nested if required.
 */
public record CategoryTreeResponse(

        UUID id,

        String name,

        String slug,

        String description,

        String iconName,

        int displayOrder,

        List<CategoryTreeResponse> children
) {
}