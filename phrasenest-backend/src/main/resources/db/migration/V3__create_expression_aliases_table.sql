CREATE TABLE expression_aliases (
    id UUID PRIMARY KEY,

    expression_id UUID NOT NULL,

    alias_text VARCHAR(500) NOT NULL,
    normalized_alias VARCHAR(500) NOT NULL,

    alias_type VARCHAR(40) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT TRUE,

    usage_note TEXT,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_expression_aliases_expression
        FOREIGN KEY (expression_id)
        REFERENCES expressions(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_expression_aliases_normalized_alias
        UNIQUE (normalized_alias),

    CONSTRAINT ck_expression_aliases_alias_type
        CHECK (
            alias_type IN (
                'SHORT_FORM',
                'FULL_FORM',
                'ALTERNATIVE_FORM',
                'SPELLING_VARIANT',
                'CONTRACTION',
                'COMMON_MISSPELLING',
                'COMMON_MISTAKE'
            )
        )
);

CREATE INDEX idx_expression_aliases_expression_id
    ON expression_aliases (expression_id);

CREATE INDEX idx_expression_aliases_alias_type
    ON expression_aliases (alias_type);

-- Supports typo-tolerant alias searching.
CREATE INDEX idx_expression_aliases_normalized_alias_trgm
    ON expression_aliases
    USING GIN (normalized_alias gin_trgm_ops);