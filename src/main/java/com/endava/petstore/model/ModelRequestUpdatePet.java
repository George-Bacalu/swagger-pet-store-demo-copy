package com.endava.petstore.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelRequestUpdatePet implements Serializable {

    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(notes = "name", dataType = "string")
    @ApiParam(value = "Updated name of the pet")
    private String name;

    @ApiModelProperty(notes = "status", dataType = "string")
    @ApiParam(value = "Updated status of the pet")
    private String status;
}
