package com.endava.petstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    PLACED("placed"), APPROVED("approved"), DELIVERED("delivered");

    private final String orderStatus;

    @Override
    @JsonProperty
    public String toString() {
        return orderStatus;
    }
}
