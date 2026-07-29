//package com.phrasenest.shared.api;
//
//import java.time.Instant;
//
//public record ApiResponse<T>(
//        boolean success,
//        String message,
//        T data,
//        Instant timestamp
//) {
//    public static <T> ApiResponse<T> success(String message, T data) {
//        return new ApiResponse<>(true, message, data, Instant.now());
//    }
//}

// improved API Response

package com.phrasenest.shared.api;

import java.time.Instant;

/**
 * Standard response wrapper used by the API.
 */
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {

    public static <T> ApiResponse<T> success(
            String message,
            T data
    ) {
        return new ApiResponse<>(
                true,
                message,
                data,
                Instant.now()
        );
    }

    public static <T> ApiResponse<T> failure(
            String message,
            T data
    ) {
        return new ApiResponse<>(
                false,
                message,
                data,
                Instant.now()
        );
    }
}