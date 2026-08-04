package com.phrasenest.expression.category.api;


import com.phrasenest.category.api.CategoryResponse;
import com.phrasenest.expression.api.ExpressionSummaryResponse;

import java.util.List;

/**
 * Combined response for a public category page.
 */
public record CategoryExpressionsResponse(

        CategoryResponse category,

        List<ExpressionSummaryResponse> expressions
) {
}