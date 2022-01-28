package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.exporter.UserCsvExporter;
import com.shopme.admin.user.exporter.UserExcelExporter;
import com.shopme.admin.user.exporter.UserPDFExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	
	@Autowired	
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage() {
		return "redirect:/users/pages/1?sortField=id&sortDir=asc";
	}
	
	@GetMapping("/users/pages/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
							 @PathVariable(name = "pageNum") int pageNum
							 ) {
		service.listByPage(pageNum, helper);
		return "users/users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnabled(true);
		
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		
		return "users/user_form";
	}
	
	// RedirectAttributes give the mess when redirect back to page
	@PostMapping("/users/save")
	public String saveUser(User user, 
						   RedirectAttributes redirecAttritibutes,
						   @RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		//check if there is a file
		if(!multipartFile.isEmpty()) { // if there is image when creating user
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		
		redirecAttritibutes.addFlashAttribute("message", "The user has been save successfully");
		
		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartofEmail = user.getEmail().split("@")[0];
		
		return "redirect:/users/pages/1?sortField=id&sortDir=asc&keyword=" + firstPartofEmail;
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, 
			RedirectAttributes redirecAttritibutes,
			Model model) {
		
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("user",user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Edit User (ID: " + id +")");
			return "users/user_form";
		} catch (UserNotFoundException ex) {
			redirecAttritibutes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, 
			RedirectAttributes redirecAttritibutes,
			Model model) {
		
		try {
			service.delete(id);
			redirecAttritibutes.addFlashAttribute("message", "The User ID " + id + " has been deleted.");
		} catch (UserNotFoundException ex) {
			redirecAttritibutes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
										  @PathVariable("status") boolean enabled,
										  RedirectAttributes redirectsAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "this user ID " + id + " has been " + status;
		redirectsAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	} 
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	} 
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserPDFExporter exporter = new UserPDFExporter();
		exporter.export(listUsers, response);
	} 
	
	
}
