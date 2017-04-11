package com.tenx.ms.retail.product.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class Product {

    @ApiModelProperty(value = "product id for product")
    private Long productId;

    @ApiModelProperty(value = "Store id of the product")
    private Long storeId;

    @ApiModelProperty(value = "name of the product")
    @NotNull
    private String name;

    @ApiModelProperty(value = "description of the product")
    private String description;


    @ApiModelProperty(value = "SKU of the product")
    @Pattern(regexp = "[a-zA-Z0-9]*")
    @Size(min = 5, max = 10)
    private String sku;

    @ApiModelProperty(value = "price of the production")
    @NumberFormat(style = NumberFormat.Style.NUMBER , pattern = "###.##")
    @NotNull
    private Double price;

}
