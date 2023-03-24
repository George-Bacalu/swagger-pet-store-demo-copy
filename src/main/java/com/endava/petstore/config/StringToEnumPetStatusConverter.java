package com.endava.petstore.config;

import com.endava.petstore.model.Status;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumPetStatusConverter implements Converter<String, Status> {

    @Override
    public Status convert(String source) {
        return Status.valueOf(source.toUpperCase());
    }
}
