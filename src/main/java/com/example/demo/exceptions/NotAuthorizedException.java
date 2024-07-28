package com.example.demo.exceptions;

import javax.annotation.Nullable;

public class NotAuthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotAuthorizedException(@Nullable String message) {
        super(message);
    }
}
