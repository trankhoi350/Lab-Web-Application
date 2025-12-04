package com.example.productmanagement.service;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProducts(Sort sort) {
        return productRepository.findAll(sort);
    }

//    @Override
//    public List<Product> getAllProducts(Sort sort) {
//        return productRepository.findAllProductsByPriceDescending(sort);
//    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        // Validation logic can go here
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable);
    }

    @Override
    public List<Product> advanceSearchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }

    @Override
    public List<String> categoryFilter() {
        return productRepository.findAllCategories();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByCategory(String category, Sort sort) {
        return productRepository.findByCategory(category, sort);
    }

    @Override
    public long getTotalCount() {
        return productRepository.count(); // Built-in JPA method
    }

    @Override
    public BigDecimal getTotalValue() {
        return productRepository.calculateTotalValue();
    }

    @Override
    public BigDecimal getAveragePrice() {
        return productRepository.calculateAveragePrice();
    }

    @Override
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts(10); // Threshold < 10
    }

    @Override
    public List<Product> getRecentProducts() {
        return productRepository.findTop5ByOrderByIdDesc();
    }

    @Override
    public Map<String, Long> getCategoryDistribution() {
        List<String> categories = productRepository.findAllCategories();
        Map<String, Long> distribution = new HashMap<>();

        // Loop through categories to count products in each
        for (String category : categories) {
            long count = productRepository.countByCategory(category);
            distribution.put(category, count);
        }
        return distribution;
    }
}
