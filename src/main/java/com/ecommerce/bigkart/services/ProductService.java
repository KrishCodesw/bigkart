package com.ecommerce.bigkart.services;

import com.ecommerce.bigkart.dto.ProductDTO;
import com.ecommerce.bigkart.entities.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private List<ProductEntity> productList=new ArrayList<>();

    // Add a product
    public ProductDTO addProduct(ProductDTO productdto){
        ProductEntity entity=new ProductEntity();
        entity.setId(productdto.getId());
        entity.setName(productdto.getName());
        entity.setPrice(productdto.getPrice());
        entity.setQuantity(productdto.getQuantity());
        // db call
        productList.add(entity);
        return productdto;
    }

    //    GetALl
    public List<ProductDTO> getAllProduct() {
        List<ProductDTO> productdtolist= new ArrayList<>();
        for(ProductEntity entity:productList){

            ProductDTO pdto=new ProductDTO();
            pdto.setId(entity.getId());
            pdto.setName(entity.getName());
            pdto.setPrice(entity.getPrice());
            pdto.setQuantity(entity.getQuantity());

            productdtolist.add(pdto);
        }
        return productdtolist;
    }

    public ProductDTO getProductById(int id){
        for(ProductEntity entity:productList){
            if(entity.getId()==id){
                ProductDTO pdto=new ProductDTO();
                pdto.setId(entity.getId());
                pdto.setName(entity.getName());
                pdto.setPrice(entity.getPrice());
                pdto.setQuantity(entity.getQuantity());
                return pdto;
            }
        }
        return null;
    }

    public ProductDTO updateProductById(int id,ProductDTO dto){
        for(ProductEntity entity:productList){
            if(entity.getId()==id){
                entity.setName(dto.getName());
                entity.setQuantity(dto.getQuantity());
                entity.setPrice(dto.getPrice());

                return dto;
            }
        }
return null;
    }


    public String deleteProductById(int id){
        for(ProductEntity entity:productList){
            if(entity.getId()==id){
                productList.remove(entity);
                return "Entity deleted";
            }
        }
        return "Entity not found";
    }

}
