package com.endava.petstore.config;

import com.endava.petstore.model.OrderStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumOrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String source) {
        return OrderStatus.valueOf(source.toUpperCase());
    }
}
