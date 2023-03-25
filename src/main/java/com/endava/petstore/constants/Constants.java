package com.endava.petstore.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String PET_NOT_FOUND = "There is no pet with id %s";
    public static final String PET_UPDATED = "Updated pet with id %s";
    public static final String TAGS_NOT_FOUND = "No tags were provided";
    public static final String IMAGE_UPLOADED = "additionalMetadata: %s%nFile uploaded to %s (%s bytes)";
    public static final String ORDER_NOT_FOUND = "There is no order with id %s";
    public static final String USER_NOT_FOUND = "There is no user with id %s";
    public static final String USERNAME_NOT_FOUND = "There is no user with username %s";
    public static final String INVALID_USER_CREDENTIALS = "There is no user with username %s and password %s";
}
