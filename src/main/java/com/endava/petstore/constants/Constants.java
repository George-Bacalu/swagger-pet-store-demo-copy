package com.endava.petstore.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String PET_NOT_FOUND = "There is no pet with id %s";
    public static final String PET_UPDATED = "Updated pet with id %s";
    public static final String TAGS_NOT_FOUND = "No tags were provided";
    public static final String IMAGE_UPLOADED = "additionalMetadata: %s%nFile uploaded to %s (%s bytes)";
}
