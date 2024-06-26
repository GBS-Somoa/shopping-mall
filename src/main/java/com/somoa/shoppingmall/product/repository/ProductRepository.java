package com.somoa.shoppingmall.product.repository;

import com.somoa.shoppingmall.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p " +
            "where p.name like concat('%', :keyword, '%') " +
            "order by p.rating desc, p.reviewCount desc, p.id")
    Page<Product> findAllByNameContaining(Pageable pageable, @Param("keyword") String keyword);

    Optional<Product> findByBarcode(String barcode);
}
