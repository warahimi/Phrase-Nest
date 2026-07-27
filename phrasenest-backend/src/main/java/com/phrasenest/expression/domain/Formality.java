package com.phrasenest.expression.domain;


/**
 * Describes how formal an expression is.
 * Examples:
 *      FORMAL:          "With all due respect..."
 *      NEUTRAL:         "I agree."
 *      INFORMAL:        "I second that."
 *      VERY_INFORMAL:   "Bug off."
 */
public enum Formality {

    FORMAL,

    NEUTRAL,

    INFORMAL,

    VERY_INFORMAL
}