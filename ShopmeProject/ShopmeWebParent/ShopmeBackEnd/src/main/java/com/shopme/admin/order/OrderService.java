package com.shopme.admin.order;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;

@Service
@Transactional
public class OrderService {
	public static final int ORDERS_PER_PAGE = 10;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private CountryRepository countryRepo;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		String sortField = helper.getSortField();
		String sortDir = helper.getSortDir();
		String keyword = helper.getKeyword();
		
		Sort sort = null;
		
		if ("destination".equals(sortField)) {
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		} else {
			sort = Sort.by(sortField);
		}
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
		
		Page<Order> page = null;

		if (keyword != null) {
			page = orderRepo.findAll(keyword, pageable);
		} else {
			page = orderRepo.findAll(pageable);
		}

		helper.updateModelAttributes(pageNum, page);
	}
	
	public Order get(Integer id) throws OrderNotFoundException{
		try {
			return orderRepo.findById(id).get();
		} catch (NoSuchElementException ex){
			throw new OrderNotFoundException("Could not find any orders with ID " + id);
		}
		
	}
	
	public void delete(Integer id) throws OrderNotFoundException{
		Long countById = orderRepo.countById(id);
		if(countById == null || countById == 0) {
			throw new OrderNotFoundException("Could not found any order with ID " + id);
		}
		
		orderRepo.deleteById(id);
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}
}
