package com.endava.petstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements Serializable {

    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", dataType = "long")
    @Positive(message = "Order id must be positive")
    private Long id;

    @ApiModelProperty(name = "name", dataType = "long")
    @NotNull(message = "Pet must not be null")
    private Long petId;

    @ApiModelProperty(name = "quantity", dataType = "int")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @ApiModelProperty(name = "shipDate", dataType = "string")
    @DateTimeFormat
    private LocalDateTime shipDate;

    @JsonProperty("status")
    @ApiModelProperty(name = "status", dataType = "string", value = "Order Status")
    @NotNull(message = "Order status must not be null")
    private OrderStatus orderStatus;

    @ApiModelProperty(name = "complete", dataType = "boolean")
    private Boolean complete;
}
