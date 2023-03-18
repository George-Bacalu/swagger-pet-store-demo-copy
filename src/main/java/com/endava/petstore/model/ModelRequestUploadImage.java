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
public class ModelRequestUploadImage implements Serializable {

    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(notes = "additionalMetadata")
    @ApiParam(value = "Additional data to pass to server")
    private String additionalMetadata;
}
