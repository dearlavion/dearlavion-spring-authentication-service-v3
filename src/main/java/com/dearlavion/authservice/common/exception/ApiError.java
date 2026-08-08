package com.dearlavion.authservice.common.exception;

public record ApiError(int statusCode, Object message, String error) {
}
