package com.fastx.live_score.core.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response wrapper for API responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> AppResponse<T> success(T data) {
        return AppResponse.<T>builder()
                .success(true)
                .message("Request successful")
                .data(data)
                .build();
    }

    public static <T> AppResponse<T> success(String message, T data) {
        return AppResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> AppResponse<T> failure(String message) {
        return AppResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
