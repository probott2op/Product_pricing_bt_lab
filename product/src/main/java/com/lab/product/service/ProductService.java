package com.lab.product.service;

import com.lab.product.DTO.CreateOrUpdateProductRequestDTO;
import com.lab.product.DTO.ProductDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDetailsDTO createProduct(CreateOrUpdateProductRequestDTO requestDTO);
    ProductDetailsDTO updateProduct(String productCode, CreateOrUpdateProductRequestDTO requestDTO);
    ProductDetailsDTO getProductById(UUID productId);
    ProductDetailsDTO getProductByCode(String productCode);
    Page<ProductDetailsDTO> getAllProducts(Pageable pageable);
    void deleteProduct(String productCode);
    List<ProductDetailsDTO> searchProducts(String productType, String status, String startDate, String endDate);
}
