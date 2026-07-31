package com.phrasenest.expression.alias.api;


import com.phrasenest.expression.alias.domain.AliasType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request body for changing an existing alias.
 */
public record UpdateExpressionAliasRequest(

        @NotBlank(message = "Alias text is required.")
        @Size(max = 500)
        String aliasText,

        @NotNull(message = "Alias type is required.")
        AliasType aliasType,

        @NotNull(message = "Correct status is required.")
        Boolean correct,

        @Size(max = 3000)
        String usageNote
) {
}
