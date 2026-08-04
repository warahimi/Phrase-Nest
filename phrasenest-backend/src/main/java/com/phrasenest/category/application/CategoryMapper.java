package com.phrasenest.category.application;


import com.phrasenest.category.api.CategoryResponse;
import com.phrasenest.category.domain.Category;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Converts Category entities into API response DTOs.
 */
@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {

        /*
         * Parent may be null for top-level categories.
         */
        Category parent = category.getParent();

        UUID parentId = parent == null
                ? null
                : parent.getId();

        String parentName = parent == null
                ? null
                : parent.getName();

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                parentId,
                parentName,
                category.getIconName(),
                category.getDisplayOrder(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
