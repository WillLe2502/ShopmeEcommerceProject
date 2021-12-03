package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
	
	@Autowired
	private BrandRepository repo;
	
//	@Test
//	public void testCreateNewBrand() {
//		Brand brand = new Brand("Samsung", "brand-logo.png");
//		Brand savedBrand = repo.save(brand);
//		
//		assertThat(savedBrand.getId()).isGreaterThan(0);
//	}
	
	@Test
	public void testCreateNewBrand1() {
		Category laptops = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);
		
		Brand savedBrand = repo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewBrand2() {
		Category cellphones = new Category(4);
		Category tablets = new Category(7);
		
		Brand apple = new Brand("Apple");
		apple.getCategories().add(cellphones);
		apple.getCategories().add(tablets);
		
		Brand savedBrand = repo.save(apple);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewBrand3() {
		Brand samsung = new Brand("Samsung");
		samsung.getCategories().add(new Category(29));
		samsung.getCategories().add(new Category(24));
		
		Brand savedBrand = repo.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindall() {
		Iterable<Brand> listBrands = repo.findAll();
		listBrands.forEach(brand -> System.out.println(brand));
		
		assertThat(listBrands).isNotEmpty();
	}
	
	@Test
	public void testGetBrandById() {
		Brand brand = repo.findById(5).get();
		System.out.println(brand);
		assertThat(brand.getName()).isEqualTo("Acer");
	}
	
	@Test
	public void testUpdateBrandDetail() {
		Brand brand = repo.findById(7).get();
		brand.setName("Samsung Electronics");
		
		Brand savedBrand = repo.save(brand);
		assertThat(savedBrand.getName()).isEqualTo("Samsung Electronics");
	}
	
	@Test
	public void testDeleteBrand() {
		Integer brandID = 6;
		repo.deleteById(brandID);
		
		Optional<Brand> result = repo.findById(brandID);
		assertThat(result.isEmpty());
	}
}
