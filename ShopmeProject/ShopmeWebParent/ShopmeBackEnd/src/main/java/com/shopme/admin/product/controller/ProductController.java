package com.shopme.admin.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.brand.BrandService;
import com.shopme.admin.product.ProductNotFoundException;
import com.shopme.admin.product.ProductService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;	
	
	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts =  productService.listAll();
		model.addAttribute("listProducts", listProducts);
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInstock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product,
							  RedirectAttributes ra) {
		productService.save(product);
		ra.addFlashAttribute("message", "The product has been save successfully.");
		
		return "redirect:/products";
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id,
											  @PathVariable("status") boolean enabled,
											  RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, 
								 RedirectAttributes redirecAttritibutes,
								 Model model) {
		try {
			productService.delete(id);
			redirecAttritibutes.addFlashAttribute("message", "The Product ID " + id + " has been deleted.");
		} catch (ProductNotFoundException ex) {
			redirecAttritibutes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/products";
	}
}
