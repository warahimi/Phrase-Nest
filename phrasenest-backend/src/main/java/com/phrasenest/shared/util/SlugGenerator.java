package com.phrasenest.shared.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Converts expression text into a URL-friendly slug.
 * Example:

 * generate("I wasn't born yesterday.");
 *
 * Result:
 *
 * i-wasnt-born-yesterday
 */
@Component
public class SlugGenerator {

    public String generate(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(
                    "Cannot generate a slug from blank text."
            );
        }

        return Normalizer
                .normalize(input, Normalizer.Form.NFKD)

                // Remove accent marks.
                .replaceAll("\\p{M}", "")

                .toLowerCase(Locale.ENGLISH)

                // Remove apostrophes completely.
                .replace("'", "")
                .replace("’", "")

                // Replace non-letter and non-number groups with hyphens.
                .replaceAll("[^a-z0-9]+", "-")

                // Remove a hyphen from the beginning.
                .replaceAll("^-+", "")

                // Remove a hyphen from the end.
                .replaceAll("-+$", "");
    }
}