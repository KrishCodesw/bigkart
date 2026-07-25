package com.ecommerce.bigkart.repository;
import com.ecommerce.bigkart.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {


}
