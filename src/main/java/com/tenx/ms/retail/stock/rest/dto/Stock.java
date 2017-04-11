package com.tenx.ms.retail.stock.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Stock {


    @ApiModelProperty("total count in stock")
    @Min(value = 0)
    @NotNull
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
