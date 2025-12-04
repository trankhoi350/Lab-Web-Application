package com.example.productmanagement.controller;

import com.example.productmanagement.entity.Product;
import com.example.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String showDashboard(Model model) {
        // 1. Fetch data from Service
        long totalCount = productService.getTotalCount();
        BigDecimal totalValue = productService.getTotalValue();
        BigDecimal avgPrice = productService.getAveragePrice();
        List<Product> lowStock = productService.getLowStockProducts();
        List<Product> recentProducts = productService.getRecentProducts();
        Map<String, Long> categoryStats = productService.getCategoryDistribution();

        // 2. Add to Model
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("avgPrice", avgPrice);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("recentProducts", recentProducts);
        model.addAttribute("categoryStats", categoryStats);

        return "dashboard";
    }
}

