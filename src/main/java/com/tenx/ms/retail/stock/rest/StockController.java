package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = RestConstants.VERSION_ONE+"/stock/{storeId}/{productId}")
public class StockController {

    @Autowired
    private StockService stockService;

    @ApiOperation("Add or update stock")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Stock updated sucessfully"),
            @ApiResponse(code = 412, message = "Precondition failiure"),
            @ApiResponse(code = 500, message = "Internal service error") })
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
    public void addOrUpdate(@ApiParam(name = "storeId") @PathVariable Long storeId,
            @ApiParam(name = "productId")  @PathVariable Long productId,@ApiParam(name = "count", value = "fields for creating store", required = true) @RequestBody @Validated Stock stock)  {

        stockService.addUpdateStock(stock.getCount(), storeId, productId);

    }
}
