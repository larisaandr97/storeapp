package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.ProductRequest;
import com.javaproject.storeapp.mapper.ProductMapper;
import com.javaproject.storeapp.entities.Product;
import com.javaproject.storeapp.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final MainService mainService;
    private final ProductMapper productMapper;

    public ProductController(MainService mainService, ProductMapper productMapper) {
        this.mainService = mainService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductRequest p) {
        Product product = productMapper.productRequestToProduct(p);

        Product createdProduct = mainService.addProduct(product);
        return ResponseEntity
                //created() will return the 201 HTTP code and set the Location header on the response, with the url to the newly created customer
                .created(URI.create("/products/" + createdProduct.getId()))
                //body() will populate the body of the response with the customer details
                .body(createdProduct);
    }

    @GetMapping("{id}")
    public Product getProduct(@PathVariable int id) {
        return mainService.findProductById(id);
    }

    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) boolean descending) {
        return mainService.getProductsBy(category, name, descending);
    }

}
