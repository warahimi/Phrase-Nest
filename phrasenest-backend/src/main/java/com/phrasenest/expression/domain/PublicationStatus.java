package com.phrasenest.expression.domain;


/**
 * Controls whether an expression can be seen by normal users.
 */
public enum PublicationStatus {

    /**
     * The expression is still being prepared.
     */
    DRAFT,

    /**
     * The expression is waiting for an editor or administrator.
     */
    PENDING_REVIEW,

    /**
     * The expression is available to application users.
     */
    PUBLISHED,

    /**
     * The expression was reviewed but not accepted.
     */
    REJECTED,

    /**
     * The expression is kept in the database but no longer displayed.
     */
    ARCHIVED
}