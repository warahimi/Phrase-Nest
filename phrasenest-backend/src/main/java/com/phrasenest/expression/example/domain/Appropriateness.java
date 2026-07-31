package com.phrasenest.expression.example.domain;


/**
 * Indicates whether the usage is appropriate in the described context.
 */
public enum Appropriateness {

    /**
     * Natural and suitable usage.
     */
    APPROPRIATE,

    /**
     * Understandable, but may not be ideal in every situation.
     */
    USE_WITH_CAUTION,

    /**
     * Grammatically or socially inappropriate.
     */
    INAPPROPRIATE
}
