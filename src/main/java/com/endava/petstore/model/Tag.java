package com.endava.petstore.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag {
    @Positive(message = "Tag id must be positive")
    private Long id;

    @NotBlank(message = "Tag name must not be blank")
    @Size(min = 3, max = 30, message = "Tag name must have between {min} and {max} characters")
    private String name;
}
