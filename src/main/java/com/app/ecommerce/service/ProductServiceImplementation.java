package com.app.ecommerce.service;

import com.app.ecommerce.exceptions.ResourceNotFoundException;
import com.app.ecommerce.model.Category;
import com.app.ecommerce.model.Product;
import com.app.ecommerce.payload.ProductDTO;
import com.app.ecommerce.payload.ProductResponseDTO;
import com.app.ecommerce.repository.CategoryRepository;
import com.app.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

   private final ProductRepository productRepository;
   private final CategoryRepository categoryRepository;
   private final ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponseDTO fetchAllProducts() {
        List<Product> productsList = productRepository.findAll();
        List<ProductDTO> productsDTO = productsList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productsDTO);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product", "categoryId", categoryId));

        List<Product> productsList = productRepository.findByCategory(category);
        List<ProductDTO> productsDTO = productsList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productsDTO);
        return productResponseDTO;
    }
}
