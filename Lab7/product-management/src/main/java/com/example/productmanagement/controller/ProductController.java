package com.example.productmanagement.controller;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // List all products
    @GetMapping
    public String listProducts(
            @RequestParam(required = false) String category, // Add this!
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        List<Product> products;

        // 1. Prepare the Sort object (Default to unsorted if null)
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDir.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        }

        // 2. Decide which data to fetch based on "category"
        if (category != null && !category.isEmpty() && !"All Categories".equals(category)) {
            // Case A: Filtered AND Sorted (using the new Service method)
            products = productService.getProductsByCategory(category, sort);
        } else {
            // Case B: All products AND Sorted
            products = productService.getAllProducts(sort);
        }

        // 3. Populate the model
        model.addAttribute("products", products);
        model.addAttribute("categories", productService.categoryFilter());

        // Pass these back to the view so "sticky" links work
        model.addAttribute("selectedCategory", category);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "product-list";
    }


//    List<String> categoryList = productService.categoryFilter();
//        model.addAttribute("categories", categoryList);

    // Show form for new product
    @GetMapping("/new")
    public String showNewForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product-form";
    }

    // Show form for editing product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found");
                    return "redirect:/products";
                });
    }

    // Save product (create or update)
    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "product-form";
        }

        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/products";
    }

    // Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.searchProducts(keyword, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "product-list";
    }


    // Filter products
    @GetMapping("/filter")
    public String filter(@RequestParam(value = "category", required = false) String category, Model model) {
        List<Product> products;
        List<String> categoryList = productService.categoryFilter();

        if (category != null && !category.isEmpty() && !"All Categories".equals(category)) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAllProducts();
        }

        // Populate the dropdown list (keep this as is)
        List<String> categories = productService.categoryFilter();

        model.addAttribute("categories", categoryList);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/advanced-search")
    public String advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {
        List<Product> products = productService.advanceSearchProducts(name, category, minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "product-list";
    }


}
