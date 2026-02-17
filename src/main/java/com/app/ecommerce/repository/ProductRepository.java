package com.app.ecommerce.repository;

import com.app.ecommerce.model.Category;
import com.app.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);

    boolean existsByProductNameAndCategoryCategoryId(String productName, Long categoryId);
}
