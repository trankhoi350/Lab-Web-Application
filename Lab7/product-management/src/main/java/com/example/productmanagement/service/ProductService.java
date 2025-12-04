package com.example.productmanagement.service;

import com.example.productmanagement.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    List<Product> getAllProducts(Sort sort);

    Optional<Product> getProductById(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Long id);

    Page<Product> searchProducts(String keyword, Pageable pageable);

    List<Product> advanceSearchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice);

    List<String> categoryFilter();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByCategory(String category, Sort sort);

    long getTotalCount();

    BigDecimal getTotalValue();

    BigDecimal getAveragePrice();

    List<Product> getLowStockProducts();

    List<Product> getRecentProducts();

    Map<String, Long> getCategoryDistribution(); // For the pie chart/list
}
