package com.shopme.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

@Service
@Transactional
public class AddressService {
	
	@Autowired
	private AddressRepository repo;
	
	public List<Address> listAddressBook(Customer customer){
		return repo.findByCustomer(customer);
	}
	
	
}
