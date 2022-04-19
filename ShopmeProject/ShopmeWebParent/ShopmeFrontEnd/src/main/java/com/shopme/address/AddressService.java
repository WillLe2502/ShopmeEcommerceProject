package com.shopme.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

@Service
@Transactional
public class AddressService {
	
	@Autowired
	private AddressRepository repo;
	
	@Autowired
	private CountryRepository countryRepo;
	
	public List<Address> listAddressBook(Customer customer){
		return repo.findByCustomer(customer);
	}
	
	public void save(Address address) {
		repo.save(address);
	}
	
	public Address get(Integer addressId, Integer CustomerId) {
		return repo.findByIdAndCustomer(addressId, CustomerId);
	}
	
	public void delete(Integer addressId, Integer CustomerId) {
		repo.deleteByIdAndCustomer(addressId, CustomerId);
	}
	
}
