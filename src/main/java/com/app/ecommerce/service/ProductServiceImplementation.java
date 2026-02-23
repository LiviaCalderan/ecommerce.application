package com.app.ecommerce.service;

import com.app.ecommerce.exceptions.APIException;
import com.app.ecommerce.exceptions.ResourceNotFoundException;
import com.app.ecommerce.model.Cart;
import com.app.ecommerce.model.Category;
import com.app.ecommerce.model.Product;
import com.app.ecommerce.payload.CartDTO;
import com.app.ecommerce.payload.ProductDTO;
import com.app.ecommerce.payload.ProductResponseDTO;
import com.app.ecommerce.repository.CartRepository;
import com.app.ecommerce.repository.CategoryRepository;
import com.app.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

   private final ProductRepository productRepository;
   private final CategoryRepository categoryRepository;
   private final CartRepository cartRepository;
   private final ModelMapper modelMapper;
   private final FileService fileService;
   private final CartService cartService;


    @Value("${project.image}")
   private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        if(productRepository.existsByProductNameAndCategoryCategoryId(productDTO.getProductName(), categoryId)){
            throw new APIException("Product already exists in this category");
        }

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
    public ProductResponseDTO fetchAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Page<Product> productPage = productRepository.findAll(buildPageable(pageNumber, pageSize, sortBy, sortOrder));

        return buildProductResponseDTO(productPage);
    }

    @Override
    public ProductResponseDTO searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Products", "categoryId", categoryId));

        Page<Product> productPage = productRepository.findByCategory(category,
                buildPageable(pageNumber, pageSize, sortBy, sortOrder));

        return buildProductResponseDTO(productPage);
    }

    @Override
    public ProductResponseDTO searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%',
                buildPageable(pageNumber, pageSize, sortBy, sortOrder));

        return buildProductResponseDTO(productPage);
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

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                    .toList();
            cartDTO.setProductDTOS(products);
            return cartDTO;
        }).toList();

        cartDTOS.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteCartItem(productId));

        productRepository.delete(productToDelete);
        return modelMapper.map(productToDelete, ProductDTO.class);

    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        Product existingProductDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);
        existingProductDB.setImage(fileName);
        Product updatedProduct = productRepository.save(existingProductDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    private ProductResponseDTO buildProductResponseDTO(Page<Product> productPage) {

//        if(productPage.getTotalElements() == 0){
//            throw new APIException("No Products Exist!!");
//        }

        List<ProductDTO> productsDTO = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponseDTO response = new ProductResponseDTO();
        response.setContent(productsDTO);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        response.setLastPage(productPage.isLast());
        return response;
    }

    private Pageable buildPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber, pageSize, sort);
    }

}
