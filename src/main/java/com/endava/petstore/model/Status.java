package com.endava.petstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    AVAILABLE("available"), PENDING("pending"), SOLD("sold");

    private final String petStatus;

    @Override
    @JsonProperty
    public String toString() {
        return petStatus;
    }
}
