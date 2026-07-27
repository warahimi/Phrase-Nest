package com.phrasenest.shared.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Creates a consistent searchable version of an expression.
 * Example:
 * normalize("  I Wasn’t Born Yesterday!!!  ");
 * Result:
 * i wasn't born yesterday

 * We do not remove apostrophes because:
 * wasn't

 * and:

 * wasnt

 * are not exactly the same spelling.
 * Later, aliases can handle common missing-apostrophe searches.
 */
@Component
public class ExpressionNormalizer {

    public String normalize(String input) {
        if (input == null) {
            return null;
        }

        return Normalizer
                // Standardize Unicode characters.
                .normalize(input, Normalizer.Form.NFKC)

                // Convert curly apostrophes into normal apostrophes.
                .replace('’', '\'')
                .replace('‘', '\'')

                // Convert to lowercase consistently.
                .toLowerCase(Locale.ENGLISH)

                // Remove punctuation only from the end.
                .replaceAll("[.!?,;:]+$", "")

                // Convert repeated whitespace into one space.
                .replaceAll("\\s+", " ")

                // Remove whitespace from the beginning and end.
                .trim();
    }
}