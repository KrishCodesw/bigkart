package com.ecommerce.bigkart.services;

import com.ecommerce.bigkart.dto.ProductDTO;
import com.ecommerce.bigkart.entities.ProductEntity;
import com.ecommerce.bigkart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private List<ProductEntity> productList=new ArrayList<>();
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    // Add a product
    public ProductDTO addProduct(ProductDTO productdto){
        ProductEntity entity = modelMapper.map(productdto,ProductEntity.class);
//        ProductEntity entity=new ProductEntity();
//        entity.setId(productdto.getId());
//        entity.setName(productdto.getName());
//        entity.setPrice(productdto.getPrice());
//        entity.setQuantity(productdto.getQuantity());
        // db call
        ProductEntity savedEntity=productRepository.save(entity);
        return modelMapper.map(savedEntity,ProductDTO.class);
    }

    //    GetALl
    public List<ProductDTO> getAllProduct() {
//        List<ProductDTO> productdtolist= new ArrayList<>();
        List<ProductEntity> productdlist= productRepository.findAll();
        return productList.stream()
                .map(entity -> modelMapper.map(entity, ProductDTO.class))
                .collect(Collectors.toList());
//        for(ProductEntity entity:productList){
//
//            ProductDTO pdto=new ProductDTO();
//            pdto.setId(entity.getId());
//            pdto.setName(entity.getName());
//            pdto.setPrice(entity.getPrice());
//            pdto.setQuantity(entity.getQuantity());
//
//            productdtolist.add(pdto);
//        }
//        return productdtolist;
    }

    public ProductDTO getProductById(int id){
        Optional<ProductEntity> optionalEntity = productRepository.findById(id);
        if (optionalEntity.isPresent()) {
            return modelMapper.map(optionalEntity.get(), ProductDTO.class);
        }
//        for(ProductEntity entity:productList){
//            if(entity.getId()==id){
//                ProductDTO pdto=new ProductDTO();
//                pdto.setId(entity.getId());
//                pdto.setName(entity.getName());
//                pdto.setPrice(entity.getPrice());
//                pdto.setQuantity(entity.getQuantity());
//                return pdto;
//            }
//        }
        return null;
    }

    public ProductDTO updateProductById(int id,ProductDTO dto){
        Optional<ProductEntity> optionalEntity = productRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ProductEntity entity = optionalEntity.get();
            entity.setName(dto.getName());
            entity.setQuantity(dto.getQuantity());
            entity.setPrice(dto.getPrice());

            ProductEntity updatedEntity = productRepository.save(entity);
            return modelMapper.map(updatedEntity, ProductDTO.class);
        }
        return null;
    }


    public String deleteProductById(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return "Entity deleted";
        }
        return "Entity not found";
    }

}
