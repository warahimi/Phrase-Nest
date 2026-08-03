CREATE TABLE expression_examples (
    id UUID PRIMARY KEY,

    expression_id UUID NOT NULL,

    example_text TEXT NOT NULL,
    context_text TEXT,

    example_type VARCHAR(40) NOT NULL,
    difficulty_level VARCHAR(40),
    appropriateness VARCHAR(40) NOT NULL,

    explanation TEXT,

    display_order INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_expression_examples_expression
        FOREIGN KEY (expression_id)
        REFERENCES expressions(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_expression_examples_type
        CHECK (
            example_type IN (
                'GENERAL',
                'WORKPLACE',
                'INFORMAL',
                'FORMAL',
                'INCORRECT_USAGE',
                'CORRECTED_USAGE'
            )
        ),

    CONSTRAINT ck_expression_examples_difficulty
        CHECK (
            difficulty_level IS NULL
            OR difficulty_level IN (
                'BEGINNER',
                'ELEMENTARY',
                'INTERMEDIATE',
                'UPPER_INTERMEDIATE',
                'ADVANCED'
            )
        ),

    CONSTRAINT ck_expression_examples_appropriateness
        CHECK (
            appropriateness IN (
                'APPROPRIATE',
                'USE_WITH_CAUTION',
                'INAPPROPRIATE'
            )
        ),

    CONSTRAINT ck_expression_examples_display_order
        CHECK (display_order >= 0)
);

CREATE INDEX idx_expression_examples_expression_id
    ON expression_examples (expression_id);

CREATE INDEX idx_expression_examples_display_order
    ON expression_examples (
        expression_id,
        display_order
    );


CREATE TABLE expression_dialogues (
    id UUID PRIMARY KEY,

    expression_id UUID NOT NULL,

    speaker_a_name VARCHAR(100),
    speaker_a_text TEXT NOT NULL,

    speaker_b_name VARCHAR(100),
    speaker_b_text TEXT NOT NULL,

    context_text TEXT,
    difficulty_level VARCHAR(40),

    display_order INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_expression_dialogues_expression
        FOREIGN KEY (expression_id)
        REFERENCES expressions(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_expression_dialogues_difficulty
        CHECK (
            difficulty_level IS NULL
            OR difficulty_level IN (
                'BEGINNER',
                'ELEMENTARY',
                'INTERMEDIATE',
                'UPPER_INTERMEDIATE',
                'ADVANCED'
            )
        ),

    CONSTRAINT ck_expression_dialogues_display_order
        CHECK (display_order >= 0)
);

CREATE INDEX idx_expression_dialogues_expression_id
    ON expression_dialogues (expression_id);

CREATE INDEX idx_expression_dialogues_display_order
    ON expression_dialogues (
        expression_id,
        display_order
    );