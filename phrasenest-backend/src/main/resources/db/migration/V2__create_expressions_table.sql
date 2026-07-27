CREATE TABLE expressions (
    id UUID PRIMARY KEY,

    canonical_text VARCHAR(500) NOT NULL,
    normalized_text VARCHAR(500) NOT NULL,
    slug VARCHAR(550) NOT NULL,

    expression_type VARCHAR(50) NOT NULL,

    short_meaning TEXT NOT NULL,
    detailed_meaning TEXT,
    literal_meaning TEXT,
    usage_notes TEXT,

    cefr_level VARCHAR(5),
    formality VARCHAR(30),
    tone VARCHAR(100),
    frequency_level VARCHAR(30),
    region VARCHAR(50),
    offensive_level VARCHAR(30) NOT NULL DEFAULT 'NONE',

    publication_status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    source_type VARCHAR(30) NOT NULL DEFAULT 'EDITORIAL',

    confidence_score DOUBLE PRECISION,

    is_featured BOOLEAN NOT NULL DEFAULT FALSE,

    view_count BIGINT NOT NULL DEFAULT 0,
    search_count BIGINT NOT NULL DEFAULT 0,
    save_count BIGINT NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_expressions_normalized_text
        UNIQUE (normalized_text),

    CONSTRAINT uk_expressions_slug
        UNIQUE (slug),

    CONSTRAINT ck_expressions_cefr_level
        CHECK (
            cefr_level IS NULL
            OR cefr_level IN ('A1', 'A2', 'B1', 'B2', 'C1', 'C2')
        ),

    CONSTRAINT ck_expressions_confidence_score
        CHECK (
            confidence_score IS NULL
            OR confidence_score BETWEEN 0 AND 1
        ),

    CONSTRAINT ck_expressions_type
        CHECK (
            expression_type IN (
                'IDIOM',
                'PHRASAL_VERB',
                'COMMON_EXPRESSION',
                'PROVERB',
                'FIXED_PHRASE',
                'SLANG_EXPRESSION',
                'BUSINESS_EXPRESSION',
                'COLLOCATION',
                'CONVERSATIONAL_RESPONSE'
            )
        ),

    CONSTRAINT ck_expressions_publication_status
        CHECK (
            publication_status IN (
                'DRAFT',
                'PENDING_REVIEW',
                'PUBLISHED',
                'REJECTED',
                'ARCHIVED'
            )
        ),

    CONSTRAINT ck_expressions_source_type
        CHECK (
            source_type IN (
                'EDITORIAL',
                'AI_GENERATED',
                'COMMUNITY_SUBMITTED',
                'IMPORTED'
            )
        )
);

CREATE INDEX idx_expressions_type
    ON expressions (expression_type);

CREATE INDEX idx_expressions_publication_status
    ON expressions (publication_status);

CREATE INDEX idx_expressions_created_at
    ON expressions (created_at);

-- This index will help with typo-tolerant searching.
-- It uses the pg_trgm extension enabled in V1.
CREATE INDEX idx_expressions_normalized_text_trgm
    ON expressions
    USING GIN (normalized_text gin_trgm_ops);