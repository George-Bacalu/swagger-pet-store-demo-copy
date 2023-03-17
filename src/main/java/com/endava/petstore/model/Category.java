package com.endava.petstore.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
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
public class Category implements Serializable {

    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", dataType = "long", required = true)
    @Positive(message = "Category id must be positive")
    private Long id;

    @ApiModelProperty(name = "name", dataType = "string")
    @NotBlank(message = "Category name must not be blank")
    @Size(min = 3, max = 30, message = "Category name must have between {min} and {max} characters")
    private String name;
}
