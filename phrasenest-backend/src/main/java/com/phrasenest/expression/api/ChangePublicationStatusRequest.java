package com.phrasenest.expression.api;


import com.phrasenest.expression.domain.PublicationStatus;
import jakarta.validation.constraints.NotNull;

/**
 * A focused request for editorial workflow changes.
 */
public record ChangePublicationStatusRequest(

        @NotNull(message = "Publication status is required.")
        PublicationStatus publicationStatus
) {
}