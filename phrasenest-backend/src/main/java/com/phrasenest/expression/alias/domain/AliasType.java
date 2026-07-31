package com.phrasenest.expression.alias.domain;


/**
 * Describes why an alternative form exists.
 *
 * An alias may be:
 * - a correct alternative
 * - a shorter form
 * - a spelling variation
 * - a common learner mistake
 */
public enum AliasType {

    /**
     * A shorter but still valid version.
     *
     * Example:
     * "wasn't born yesterday"
     */
    SHORT_FORM,

    /**
     * A fully written version of a contracted expression.
     *
     * Example:
     * "I was not born yesterday."
     */
    FULL_FORM,

    /**
     * Another accepted version of the same expression.
     */
    ALTERNATIVE_FORM,

    /**
     * An accepted regional or spelling variation.
     */
    SPELLING_VARIANT,

    /**
     * A version that uses a contraction.
     *
     * Example:
     * "can't" instead of "cannot"
     */
    CONTRACTION,

    /**
     * A common incorrect spelling entered by learners.
     *
     * Example:
     * "I wasnt born yesterday"
     */
    COMMON_MISSPELLING,

    /**
     * A commonly used but grammatically incorrect form.
     *
     * Example:
     * "I am not born yesterday"
     */
    COMMON_MISTAKE
}