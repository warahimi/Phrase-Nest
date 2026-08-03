package com.phrasenest.expression.api;


import com.phrasenest.expression.alias.api.ExpressionAliasResponse;
import com.phrasenest.expression.dialogue.api.ExpressionDialogueResponse;
import com.phrasenest.expression.example.api.ExpressionExampleResponse;

import java.util.List;

/**
 * Complete expression details used by the frontend detail page.
 */
public record ExpressionDetailsResponse(
        ExpressionResponse expression,
        List<ExpressionAliasResponse> aliases,
        List<ExpressionExampleResponse> examples,
        List<ExpressionDialogueResponse> dialogues
) {
}
