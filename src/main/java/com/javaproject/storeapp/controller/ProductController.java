package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.ProductRequest;
import com.javaproject.storeapp.entity.Product;
import com.javaproject.storeapp.mapper.ProductMapper;
import com.javaproject.storeapp.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@Api(value = "/products",
        tags = "Products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    @ApiOperation(value = "Create a Product",
            notes = "Creates a new Product based on the information received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The Product was successfully created based on the received request"),
            @ApiResponse(code = 400, message = "Validation error on the received request")
    })
    public ResponseEntity<Product> addProduct(@Valid
                                              @RequestBody
                                              @ApiParam(name = "product", value = "Product details", required = true)
                                                      ProductRequest productRequest) {
        Product product = productMapper.productRequestToProduct(productRequest);

        Product createdProduct = productService.createProduct(product);

        return ResponseEntity
                //created() will return the 201 HTTP code and set the Location header on the response, with the url to the newly created customer
                .created(URI.create("/products/" + createdProduct.getId()))
                //body() will populate the body of the response with the customer details
                .body(createdProduct);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get Product",
            notes = "Get a Product based on the Id received in the request")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The Product with the entered Id does not exist!")
    })
    public Product getProduct(@PathVariable int id) {
        return productService.findProductById(id);
    }

    @GetMapping
    @ApiOperation(value = "Get all Products",
            notes = "Retrieves all Products and filter them if wanted")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The data was retrieved successfully"),
            @ApiResponse(code = 404, message = "Products with the entered properties were not found")
    })

    public ResponseEntity<?> getAllProducts(
            @RequestParam(required = false)
            @ApiParam(name = "category", value = "Product category", allowableValues = ("FASHION, SUPERMARKET, LAPTOPS, PHONES, HOME, BOOKS, TOYS"))
                    String category,
            @RequestParam(required = false)
            @ApiParam(name = "name", value = "Product name")
                    String name,
            @RequestParam(required = false)
            @ApiParam(name = "descending", value = "Boolean value indicating if values will be displayed descending by price")
                    boolean descending) {

        List<Product> productsFound = productService.getProductsBy(category, name, descending);

        if (productsFound.size() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Products with the entered properties were not found");
        else return ResponseEntity
                .ok()
                .body(productsFound);

    }

}
