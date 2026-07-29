package com.phrasenest.shared.exception;

/**
 * Thrown when a requested database record does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
