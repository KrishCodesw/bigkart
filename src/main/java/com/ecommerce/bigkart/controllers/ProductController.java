package com.ecommerce.bigkart.controllers;

import com.ecommerce.bigkart.dto.ProductDTO;
import com.ecommerce.bigkart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;


    @PostMapping("/add")
    public ProductDTO addProduct(@RequestBody ProductDTO dto){
        return productService.addProduct(dto);
    }

    @GetMapping("/getAll")
    public List<ProductDTO> getAllProducts(){
        return productService.getAllProduct();
    }

    @GetMapping("/getById/{id}")
    public ProductDTO getProductById(@PathVariable int id){
        return productService.getProductById(id);
    }
    @PutMapping("/updateById")
    public ProductDTO updateById(@RequestBody ProductDTO dto){
        return productService.updateProductById(dto.getId(),dto);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable int id){
        return productService.deleteProductById(id);
    }
}
