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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

   private final ProductRepository productRepository;
   private final CategoryRepository categoryRepository;
   private final ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
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

    @Override
    public ProductResponseDTO searchByKeyword(String keyword) {
        List<Product> productsList = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> productsDTO = productsList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productsDTO);
        return productResponseDTO;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO updateProductDTO) {
        Product existingProductDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(updateProductDTO, Product.class);
        existingProductDB.setProductName(product.getProductName());
        existingProductDB.setDescription(product.getDescription());
        existingProductDB.setQuantity(product.getQuantity());
        existingProductDB.setPrice(product.getPrice());
        existingProductDB.setDiscount(product.getDiscount());

        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        existingProductDB.setSpecialPrice(specialPrice);

        Product updatedProduct = productRepository.save(existingProductDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(productToDelete);
        return modelMapper.map(productToDelete, ProductDTO.class);

    }
}
