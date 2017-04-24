package com.tenx.ms.retail.order.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.service.OrderService;
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
@RequestMapping(value = RestConstants.VERSION_ONE+"/orders/{storeId}")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("create a new order")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Store create sucessfully"),
            @ApiResponse(code = 412, message = "Precondition failiure"),
            @ApiResponse(code = 500, message = "Internal service error") })
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
    public Order createOrder(@ApiParam(name = "storeId", value = "store id for the order") @PathVariable Long storeId,
    @ApiParam(name = "order", required = true, value = "Request of new order") @Validated
    @RequestBody Order order) {
        return orderService.createOrder(storeId, order);

    }


}
