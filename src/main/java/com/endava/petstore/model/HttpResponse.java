package com.endava.petstore.model;

import io.swagger.annotations.ApiModel;
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
public class HttpResponse implements Serializable {

    private static final Long serialVersionUID = 1L;

    @ApiParam(name = "fileCode", value = "fileCode")
    private Integer code;

    @ApiParam(name = "type", value = "type")
    private String type;

    @ApiParam(name = "message", value = "message")
    private String message;
}
