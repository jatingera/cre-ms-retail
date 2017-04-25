package com.tenx.ms.retail.order.rest.dto;

import com.tenx.ms.commons.validation.constraints.Email;
import com.tenx.ms.commons.validation.constraints.PhoneNumber;
import com.tenx.ms.retail.constant.OrderStatusConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

@Data
public class Order {

    @ApiModelProperty( "email" )
    @NotNull
    @Email
    private String email;

    @ApiModelProperty( "first name" )
    @NotNull
    @Pattern( regexp = "[A-Za-z0-9]*" )
    private String firstName;

    @ApiModelProperty( "last name" )
    @NotNull
    @Pattern( regexp = "[A-Za-z0-9]*" )
    private String lastName;

    @ApiModelProperty( "order id of a store" )
    private Long orderId;

    @ApiModelProperty( "order date" )
    private Timestamp orderDate;

    @ApiModelProperty( "phone" )
    @NotNull
    @PhoneNumber
    private String phone;

    @ApiModelProperty( "status of the order" )
    private OrderStatusConstant orderStatus;

    @ApiModelProperty( value = "store id associated with this product" )
    private Long storeId;

    @ApiModelProperty( " number or products to be purchased with quanitity and order status of each item." )
    @NotNull
    private List<OrderItem> productItems;
}
