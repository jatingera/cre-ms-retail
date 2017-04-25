package com.tenx.ms.retail.product.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
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
@RequestMapping( RestConstants.VERSION_ONE + "/products/{storeId}" )
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation( "Add a product" )
    @ApiResponses( value = {@ApiResponse( code = 200, message = "Store create sucessfully" ),
            @ApiResponse( code = 412, message = "Precondition failiure" ),
            @ApiResponse( code = 500, message = "Internal service error" )} )
    @RequestMapping( method = RequestMethod.POST, consumes = {"application/json"} )
    public ResourceCreated<Long> addProduct(
            @ApiParam( name = "storeId", value = "id of the store", required = true ) @PathVariable @Validated Long storeId,
            @ApiParam( name = "product", required = true, value = "product details to be created" ) @Validated @RequestBody
                    Product product) {

        return productService.addProduct(product, storeId);
    }

    @ApiOperation( "List of products" )
    @ApiResponses( value = {@ApiResponse( code = 200, message = "successfully returns the list of products" ),
            @ApiResponse( code = 500, message = "Internal service error" )} )
    @RequestMapping( method = RequestMethod.GET, consumes = {"application/json"} )
    public List<Product> getProducts(@ApiParam( name = "storeId", value = "id of the store", required = true ) @PathVariable @Validated Long storeId) {

        return productService.getListOfProducts(storeId);
    }

    @ApiOperation( "Get a product" )
    @ApiResponses( value = {@ApiResponse( code = 200, message = "Success" ),
            @ApiResponse( code = 500, message = "Internal service error" ), @ApiResponse( code = 404, message = "Not found" )} )
    @RequestMapping( value = "/{productField}", method = RequestMethod.GET, consumes = {"application/json"} )
    public Product getProduct(
            @ApiParam( name = "storeId", value = "id of the store", required = true ) @PathVariable @Validated Long storeId,
            @ApiParam( name = "productId", value = "Name or Id of the product", required = true ) @PathVariable @Validated String productField) {

        return productService.getProductByIdOrName(productField, storeId);
    }
}
