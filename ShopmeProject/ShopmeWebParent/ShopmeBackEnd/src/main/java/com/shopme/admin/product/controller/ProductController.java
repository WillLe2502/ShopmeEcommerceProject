package com.shopme.admin.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.product.ProductService;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts =  service.listAll();
		model.addAttribute("listProducts", listProducts);
		return "products/products";
	}
}
