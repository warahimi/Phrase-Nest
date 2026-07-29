package com.phrasenest.shared.exception;

/**
 * Thrown when a unique value already exists.
 *
 * Example:
 * Two expressions cannot have the same normalized text.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
