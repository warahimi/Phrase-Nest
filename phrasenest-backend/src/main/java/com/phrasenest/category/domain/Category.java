package com.phrasenest.category.domain;


import com.phrasenest.shared.domain.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents a browsable subject, theme, or usage situation.
 *
 * Examples:
 * - Agreement
 * - Workplace
 * - Emotions
 * - Travel
 * - Meetings
 *
 * A category may optionally have a parent category. This allows us to build
 * category hierarchies such as:
 *
 * Workplace
 *   ├── Meetings
 *   ├── Negotiation
 *   └── Presentations
 */
@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categories_name",
                        columnNames = "name"
                ),
                @UniqueConstraint(
                        name = "uk_categories_slug",
                        columnNames = "slug"
                )
        },
        indexes = {
                @Index(
                        name = "idx_categories_parent_id",
                        columnList = "parent_id"
                ),
                @Index(
                        name = "idx_categories_active",
                        columnList = "is_active"
                ),
                @Index(
                        name = "idx_categories_display_order",
                        columnList = "display_order"
                )
        }
)
public class Category extends BaseEntity {

    /**
     * Primary key for the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * User-facing category name.
     *
     * Example:
     * "Workplace"
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * URL-friendly category identifier.
     *
     * Example:
     * "workplace"
     *
     * Public URL:
     * /categories/workplace
     */
    @Column(name = "slug", nullable = false, length = 120)
    private String slug;

    /**
     * Optional explanation of the category.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Optional reference to another category that acts as the parent.
     *
     * FetchType.LAZY prevents Hibernate from loading the parent category
     * every time a category is retrieved.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "parent_id",
            foreignKey = @ForeignKey(
                    name = "fk_categories_parent"
            )
    )
    private Category parent;

    /**
     * Optional frontend icon name.
     *
     * Example:
     * "briefcase"
     * "heart"
     * "message-circle"
     *
     * We store an icon identifier rather than a full URL.
     */
    @Column(name = "icon_name", length = 50)
    private String iconName;

    /**
     * Controls category ordering in menus and browse pages.
     *
     * Smaller values appear first.
     */
    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    /**
     * Inactive categories remain in the database but are hidden from
     * public browsing.
     */
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    /**
     * Required by JPA.
     */
    protected Category() {
    }

    /**
     * Creates a new category.
     */
    public Category(
            String name,
            String slug,
            String description,
            Category parent,
            String iconName,
            int displayOrder,
            boolean active
    ) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parent = parent;
        this.iconName = iconName;
        this.displayOrder = displayOrder;
        this.active = active;
    }

    /**
     * Updates the editable category fields.
     */
    public void update(
            String name,
            String slug,
            String description,
            Category parent,
            String iconName,
            int displayOrder,
            boolean active
    ) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parent = parent;
        this.iconName = iconName;
        this.displayOrder = displayOrder;
        this.active = active;
    }

    /**
     * Makes the category visible to application users.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Hides the category from public browsing without deleting it.
     */
    public void deactivate() {
        this.active = false;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public Category getParent() {
        return parent;
    }

    public String getIconName() {
        return iconName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public boolean isActive() {
        return active;
    }
}

/**
 Why not add List<Category> children inside the entity?

 We could write:

 @OneToMany(mappedBy = "parent")
 private List<Category> children;

 However, it can cause:

 unnecessary child loading
 recursive JSON problems
 accidental large queries
 complicated entity serialization

 Instead, the repository and service will load children explicitly when needed.
 */
