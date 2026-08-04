/*
 * Creates the hierarchical category table.
 */
CREATE TABLE categories (
    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    slug VARCHAR(120) NOT NULL,

    description TEXT,

    /*
     * A null parent_id means this is a top-level category.
     */
    parent_id UUID,

    icon_name VARCHAR(50),

    display_order INTEGER NOT NULL DEFAULT 0,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_categories_name
        UNIQUE (name),

    CONSTRAINT uk_categories_slug
        UNIQUE (slug),

    CONSTRAINT fk_categories_parent
        FOREIGN KEY (parent_id)
        REFERENCES categories(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_categories_display_order
        CHECK (display_order >= 0),

    /*
     * Prevents a database row from directly referencing itself.
     *
     * More complex circular relationships are prevented in Java.
     */
    CONSTRAINT ck_categories_not_own_parent
        CHECK (
            parent_id IS NULL
            OR parent_id <> id
        )
);

CREATE INDEX idx_categories_parent_id
    ON categories (parent_id);

CREATE INDEX idx_categories_active
    ON categories (is_active);

CREATE INDEX idx_categories_display_order
    ON categories (
        display_order,
        name
    );


/*
 * Join table connecting expressions and categories.
 */
CREATE TABLE expression_categories (
    expression_id UUID NOT NULL,
    category_id UUID NOT NULL,

    is_primary BOOLEAN NOT NULL DEFAULT FALSE,

    assigned_at TIMESTAMPTZ NOT NULL,

    /*
     * The pair is the primary key, so the same category cannot be assigned
     * twice to the same expression.
     */
    PRIMARY KEY (
        expression_id,
        category_id
    ),

    CONSTRAINT fk_expression_categories_expression
        FOREIGN KEY (expression_id)
        REFERENCES expressions(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_expression_categories_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_expression_categories_expression_id
    ON expression_categories (expression_id);

CREATE INDEX idx_expression_categories_category_id
    ON expression_categories (category_id);

/*
 * PostgreSQL partial unique index.
 *
 * This guarantees that an expression can have no more than one primary
 * category.
 */
CREATE UNIQUE INDEX uk_expression_categories_one_primary
    ON expression_categories (expression_id)
    WHERE is_primary = TRUE;