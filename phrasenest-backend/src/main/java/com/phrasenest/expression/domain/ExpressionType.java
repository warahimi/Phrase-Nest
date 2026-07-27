package com.phrasenest.expression.domain;

/**
 * Identifies the linguistic type of an expression.
 *
 * We store the enum name in PostgreSQL as text.
 */
public enum ExpressionType {

    /**
     * A figurative expression whose meaning is not completely literal.
     * Example: "Break the ice."
     */
    IDIOM,

    /**
     * A verb combined with an adverb or preposition.
     * Example: "Give up."
     */
    PHRASAL_VERB,

    /**
     * A frequently used conversational expression.
     * Example: "I second that."
     */
    COMMON_EXPRESSION,

    /**
     * A traditional sentence expressing advice or general wisdom.
     * Example: "Better late than never."
     */
    PROVERB,

    /**
     * A phrase normally used in a fixed form.
     * Example: "As a matter of fact."
     */
    FIXED_PHRASE,

    /**
     * An informal expression used in casual communication.
     * Example: "My bad."
     */
    SLANG_EXPRESSION,

    /**
     * An expression commonly used in professional situations.
     * Example: "Let's touch base."
     */
    BUSINESS_EXPRESSION,

    /**
     * A natural combination of words.
     * Example: "Make a decision."
     */
    COLLOCATION,

    /**
     * A natural conversational reply.
     * Example: "You can say that again."
     */
    CONVERSATIONAL_RESPONSE
}