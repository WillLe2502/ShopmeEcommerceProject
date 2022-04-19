package com.shopme.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTest {
	
	@Autowired
	private AddressRepository repo;
	
	@Test
	public void testCreateNew() {
		Integer customerId = 5;
		Integer countryId = 234; // USA

		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Charles");
		newAddress.setLastName("Brugger");
		newAddress.setPhoneNumber("646-232-3902");
		newAddress.setAddressLine1("204 Morningview Lane");
		newAddress.setCity("New York");
		newAddress.setState("New York");
		newAddress.setPostalCode("10013");

		Address savedAddress = repo.save(newAddress);

		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
		
//		newAddress.setFirstName(hoangAnh.getFirstName());
//		newAddress.setLastName(hoangAnh.getLastName());
//		newAddress.setPhoneNumber(hoangAnh.getPhoneNumber());
//		newAddress.setAddressLine1("11 abc st");
//		newAddress.setAddressLine2("");
//		newAddress.setCity("New Delhi");
//		newAddress.setState("ABC");
//		newAddress.setPostalCode("123456");
//		newAddress.setCustomer(hoangAnh);
//		newAddress.setCountry(india);
//		newAddress.setDefaultForShipping(false);
//		
//		Address savedAddress = repo.save(newAddress);
//		assertThat(savedAddress).isNotNull();
//		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNew2() {
		Integer customerId = 1;
		Integer countryId = 16; // USA

		Address newAddress = new Address();
		
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Abbe");
		newAddress.setLastName("Seimei");
		newAddress.setPhoneNumber("123-456-7890");
		newAddress.setAddressLine1("204 CumberLand HW");
		newAddress.setCity("New Delhi");
		newAddress.setState("AEZ");
		newAddress.setPostalCode("789452");

		Address savedAddress = repo.save(newAddress);

		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId = 5;
		List<Address> listAddresses = repo.findByCustomer(new Customer(customerId));
		assertThat(listAddresses.size()).isGreaterThan(0);

		listAddresses.forEach(System.out::println);
	}

	@Test
	public void testFindByIdAndCustomer() {
		Integer addressId = 1;
		Integer customerId = 5;

		Address address = repo.findByIdAndCustomer(addressId, customerId);

		assertThat(address).isNotNull();
		System.out.println(address);
	}

	@Test
	public void testUpdate() {
		Integer addressId = 3;
//		String phoneNumber = "646-232-3932";

		Address address = repo.findById(addressId).get();
//		address.setPhoneNumber(phoneNumber);
		address.setDefaultForShipping(true);

		Address updatedAddress = repo.save(address);
//		assertThat(updatedAddress.getPhoneNumber()).isEqualTo(phoneNumber);
	}

	@Test
	public void testDeleteByIdAndCustomer() {
		Integer addressId = 1;
		Integer customerId = 5;

		repo.deleteByIdAndCustomer(addressId, customerId);

		Address address = repo.findByIdAndCustomer(addressId, customerId);
		assertThat(address).isNull();
	}
	
	@Test
	public void testSetDefault() {	
		Integer addressId = 4;
		repo.setDefaultAddress(addressId);
		
		Address address = repo.findById(addressId).get();
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	
	@Test
	public void testSetNonDefaultAddress() {
		Integer addressId = 3;
		Integer customerId = 1;
		repo.setNoneDefaultForOther(addressId, customerId);
		
		
	}
}
