package com.javaproject.storeapp.controller;

import com.javaproject.storeapp.dto.ProductRequest;
import com.javaproject.storeapp.entity.Product;
import com.javaproject.storeapp.entity.ProductCategory;
import com.javaproject.storeapp.mapper.ProductMapper;
import com.javaproject.storeapp.service.ImageService;
import com.javaproject.storeapp.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/products")
@CrossOrigin(origins = {"http://localhost:3000"})
@Api(value = "/products",
        tags = "Products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ImageService imageService;

    public ProductController(ProductService productService, ProductMapper productMapper, ImageService imageService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.imageService = imageService;
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        List<ProductCategory> categoriesAll = Arrays.asList(ProductCategory.values());
        model.addAttribute("productRequest", new ProductRequest());
        model.addAttribute("categoriesAll", categoriesAll);
        return "addProduct";
    }

    @PostMapping
    public String addProduct(@Valid
                             @RequestBody
                             @ModelAttribute
                                     ProductRequest productRequest,
                             BindingResult bindingResult,
                             @RequestParam("imagefile") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "addProduct";
        }
        Product product = productMapper.productRequestToProduct(productRequest);
        Product createdProduct = productService.createProduct(product);
        imageService.saveImageFile(createdProduct.getId(), file);
        return "redirect:/products/";
//        return ResponseEntity
//                //created() will return the 201 HTTP code and set the Location header on the response, with the url to the newly created customer
//                //   .created()//URI.create("/products/" + createdProduct.getId()))
//                //body() will populate the body of the response with the customer details
//                .status(HttpStatus.CREATED)
//                .body(product.getId());
    }

    @GetMapping("{id}")
    public Product getProduct(@PathVariable int id) {
        return productService.findProductById(id);
    }

    @GetMapping
    public ModelAndView getAllProducts(
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
        ModelAndView modelAndView = new ModelAndView("products");
        modelAndView.addObject("products", productsFound);
        return modelAndView;
        /* if (productsFound.size() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Products with the entered properties were not found");
        else return ResponseEntity
                .ok()
                .body(productsFound);*/

    }

}
