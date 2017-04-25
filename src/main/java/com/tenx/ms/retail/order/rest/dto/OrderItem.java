package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderItem {

    @ApiModelProperty( "order item id" )
    private Long orderItemId;

    @ApiModelProperty( "quantity in the stock of this item" )
    @Min( value = 0 )
    @NotNull
    private Integer quantity;

    @ApiModelProperty( "product id" )
    @NotNull
    private Long productId;

    @ApiModelProperty( "Status of this item" )
    private Boolean isItemBackordered;
}
