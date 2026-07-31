package com.phrasenest.expression.alias.api;


import com.phrasenest.expression.alias.domain.AliasType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body for adding an alias to an expression.
 */
public record CreateExpressionAliasRequest(

        @NotBlank(message = "Alias text is required.")
        @Size(
                max = 500,
                message = "Alias text cannot exceed 500 characters."
        )
        String aliasText,

        @NotNull(message = "Alias type is required.")
        AliasType aliasType,

        /**
         * True:
         * This is a valid alternative form.
         *
         * False:
         * This is a mistake or misspelling used only for search correction.
         */
        @NotNull(message = "Correct status is required.")
        Boolean correct,

        @Size(
                max = 3000,
                message = "Usage note cannot exceed 3000 characters."
        )
        String usageNote
) {
}
