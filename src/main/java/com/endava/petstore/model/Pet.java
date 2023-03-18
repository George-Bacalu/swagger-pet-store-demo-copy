package com.endava.petstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
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
public class Pet implements Serializable {

    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", dataType = "long", required = true)
    @Positive(message = "Pet id must be positive")
    private Long id;

    @ApiModelProperty(name = "name", dataType = "string", example = "doggie", required = true)
    @NotBlank(message = "Pet name must not be blank")
    @Size(min = 3, max = 30, message = "Pet name must have between {min} and {max} characters")
    private String name;

    @ApiModelProperty(name = "category")
    @NotNull(message = "Pet category must not be null")
    private Category category;

    @ApiModelProperty(name = "photoUrls", dataType = "array", required = true)
    @NotNull(message = "Pet photo URLs list must not be null")
    private List<String> photoUrls;

    @ApiModelProperty(name = "tags", dataType = "array")
    @NotNull(message = "Pet tags list must not be null")
    private List<Tag> tags;

    @JsonProperty("status")
    @ApiModelProperty(name = "status", dataType = "string", value = "pet status in store")
    @NotNull(message = "Pet status must not be null")
    private Status status;
}
