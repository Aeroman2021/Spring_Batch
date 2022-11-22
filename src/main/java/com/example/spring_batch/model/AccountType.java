package com.example.spring_batch.model;

import javafx.scene.layout.Priority;

import java.util.Arrays;
import java.util.stream.Stream;

public enum AccountType {
    SAVINGS,
    RECURRING_DEPOSIT,
    FIXED_DEPOSIT;

    public static AccountType of(String name) {
        return Stream.of(values())
                .filter(p -> p.name().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
