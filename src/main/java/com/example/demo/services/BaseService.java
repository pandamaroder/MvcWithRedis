package com.example.demo.services;

import java.util.Locale;

public abstract class BaseService {

    protected String cleanData(String data) {
        return data
            .trim()
            .toLowerCase(Locale.ROOT);
    }

}
