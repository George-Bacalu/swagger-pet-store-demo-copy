package com.endava.petstore.model;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class Pet {
    @Positive(message = "Pet id must be positive")
    private Long id;

    @NotBlank(message = "Pet name must not be blank")
    @Size(min = 3, max = 30, message = "Pet name must have between {min} and {max} characters")
    private String name;

    @NotNull(message = "Pet category must not be null")
    private Category category;

    @NotNull(message = "Pet photo URLs list must not be null")
    private List<String> photoUrls;

    @NotNull(message = "Pet tags list must not be null")
    private List<Tag> tags;

    @NotNull(message = "Pet status must not be null")
    private Status status;
}
