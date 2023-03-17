package com.endava.petstore.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String PET_NOT_FOUND = "There is no pet with id %s";
    public static final String INVALID_REQUEST_BODY = "Invalid object received on the request body";
}
