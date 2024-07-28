package com.example.demo.exceptions;

import javax.annotation.Nullable;

public class NotFoundException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(@Nullable String message) {
        super(message);
    }
}
