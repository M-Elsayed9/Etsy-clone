package com.etsyclone.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<ProductDTO> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "home";
    }

    @GetMapping("/product")
    public String showProduct(String name, Model model) {
        ProductDTO product = productService.getProduct(name);
        model.addAttribute("productDTO", product);
        return "product";
    }

    @GetMapping("/update")
    public String showUpdateForm(Long id, Model model) {
        ProductDTO product = productService.getProduct(id);
        model.addAttribute("productDTO", product);
        return "product";
    }
}
