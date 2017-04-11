package com.tenx.ms.retail.store.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("Store info")
public class Store {

    @ApiModelProperty("store id")
    private Long id;

    @ApiModelProperty("store name")
    @NotNull
    private String name;

    }
