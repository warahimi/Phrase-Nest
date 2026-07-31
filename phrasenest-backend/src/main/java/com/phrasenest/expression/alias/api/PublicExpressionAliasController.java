package com.phrasenest.expression.alias.api;


import com.phrasenest.expression.alias.application.ExpressionAliasService;
import com.phrasenest.shared.api.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Public alias-resolution endpoint.
 */
@RestController
@RequestMapping("/api/v1/public/expression-aliases")
@Validated
public class PublicExpressionAliasController {

    private final ExpressionAliasService aliasService;

    public PublicExpressionAliasController(
            ExpressionAliasService aliasService
    ) {
        this.aliasService = aliasService;
    }

    /**
     * Resolves an alias to its canonical expression.
     *
     * Example:
     *
     * GET /api/v1/public/expression-aliases/resolve
     *     ?query=I%20was%20not%20born%20yesterday
     */
    @GetMapping("/resolve")
    public ApiResponse<AliasResolutionResponse> resolveAlias(

            @RequestParam
            @NotBlank(message = "Search query is required.")
            @Size(
                    max = 500,
                    message = "Search query cannot exceed 500 characters."
            )
            String query
    ) {
        return ApiResponse.success(
                "Expression alias resolved successfully.",
                aliasService.resolvePublicAlias(query)
        );
    }
}
