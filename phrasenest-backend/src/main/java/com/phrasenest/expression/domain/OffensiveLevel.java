package com.phrasenest.expression.domain;

/**
 * Helps learners understand whether an expression may be rude or offensive.
 */
public enum OffensiveLevel {

    /**
     * Generally not offensive.
     */
    NONE,

    /**
     * May sound slightly impolite in some situations.
     */
    MILD,

    /**
     * Clearly rude.
     */
    RUDE,

    /**
     * Offensive and generally inappropriate.
     */
    OFFENSIVE,

    /**
     * Highly offensive or abusive.
     */
    HIGHLY_OFFENSIVE
}