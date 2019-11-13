package com.haze.vsail.bus.web.controller;

import com.haze.vsail.bus.entity.Product;
import com.haze.vsail.bus.service.ProductService;
import com.haze.web.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/v/product")
public class ProductController extends BaseCrudController<Product, Long> {

    private ProductService productService;

    public ProductController(ProductService productService) {
        super("vsail", "product", "供应商", productService);
        this.productService = productService;
    }

}
