package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shopme.admin.paging.PagingAndSortingArgumentResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	//expose photo to client
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		
//		// User Photos
//		exposeDirectory("user-photos", registry);
//		
//		// Category Images
//		exposeDirectory("../categories-images", registry);
//		
//		// Brand Photos
//		exposeDirectory("../brand-logos", registry);
//		
//		// Product Photos
//		exposeDirectory("../product-images", registry);
//		
//		// Site Logo
//		exposeDirectory("../site-logo", registry);
//	}
//	
//	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
//		Path path = Paths.get(pathPattern);
//		
//		String absolutePath = path.toFile().getAbsolutePath();
//		
//		String logicalPath = pathPattern.replace("../", "") + "/**";
//		
//		registry.addResourceHandler(logicalPath)
//		.addResourceLocations("file:/" + absolutePath + "/");
//	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingArgumentResolver());
	}
	
	
}
