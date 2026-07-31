package com.phrasenest.expression.example.domain;


/**
 * Describes the kind of example shown to the learner.
 */
public enum ExampleType {

    /**
     * A normal example sentence.
     */
    GENERAL,

    /**
     * An example used in a workplace or professional situation.
     */
    WORKPLACE,

    /**
     * An example from an informal conversation.
     */
    INFORMAL,

    /**
     * An example appropriate for formal communication.
     */
    FORMAL,

    /**
     * An intentionally incorrect usage.
     *
     * This should normally be accompanied by an explanation.
     */
    INCORRECT_USAGE,

    /**
     * The corrected version of an incorrect example.
     */
    CORRECTED_USAGE
}