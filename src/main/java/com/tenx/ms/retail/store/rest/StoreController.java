package com.tenx.ms.retail.store.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.store.service.StoreService;
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

import java.util.List;

@RestController
@RequestMapping(RestConstants.VERSION_ONE+"/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @ApiOperation("Get the list of stores")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),@ApiResponse(code = 500, message = "Internal service error")})
    @RequestMapping(method = RequestMethod.GET, consumes = "application/json")
    public List<Store> getAll() {
        return storeService.findAllStores();
    }

    @ApiOperation("create a store")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Store create sucessfully"),
            @ApiResponse(code = 412, message = "Precondition failiure"),
            @ApiResponse(code = 500, message = "Internal service error") })
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
    public Long createStore(@ApiParam(name = "store", value = "fields for creating store", required = true) @RequestBody @Validated Store store) {

        Long store_id = storeService.createStore(store);
        return store_id;
    }



    @ApiOperation("get a store")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "store get successfully"),
            @ApiResponse(code = 404, message = "Store not found"),
            @ApiResponse(code = 500, message = "Internal service error") })
    @RequestMapping(value = "/{storeField}", method = RequestMethod.GET, consumes = { "application/json" })
    public Store getStore(@ApiParam(name = "storeField", value = "Name or Id of the store", required = true) @PathVariable @Validated String storeField) {

             Store store = storeService.getStore(storeField);
             return store;
    }

    @ApiOperation("Delete a store")
    @ApiResponses({ @ApiResponse(code = 200, message = "Resource Found"),
            @ApiResponse(code = 404, message = "Resource Not Found"),
            @ApiResponse(code = 412, message = "Validation Error"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    @RequestMapping(method = RequestMethod.DELETE, value = "/{storeId}")
    public void delete(
            @ApiParam(name = "storeId", value = "ID of Store to be deleted") @PathVariable("storeId") Long storeId) {
        storeService.deleteStore(storeId);
    }



}
