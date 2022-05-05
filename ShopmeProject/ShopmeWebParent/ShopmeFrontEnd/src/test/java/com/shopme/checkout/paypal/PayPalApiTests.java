package com.shopme.checkout.paypal;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {
	private static final String BASE_URL = "https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	private static final String CLIENT_ID = "AalGGgZo3JRQhrCndsuT3TQy7yvManWMP9FpyszdbqRRcThRlySeK0sokiLmsKLixFginFXzB7HcZHNK";
	private static final String CLIENT_SECRET = "EKP6fw6k52gwMzTdB-_H0rBnSm1Wb2Io0-1lgAdBGKYnWonQZNVlNfHjJAyK347_LBqdyJA0dE2EDSEQ";
	
	@Test
	public void testGetOrderDetails() {
		String orderId = "51244271WU381131T";
		String requestURL = BASE_URL + GET_ORDER_API + orderId;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en_US");
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
		PayPalOrderResponse orderResponse = response.getBody();
		
		System.out.println("Order ID: " + orderResponse.getId());
		System.out.println("Validated " + orderResponse.validate(orderId));
		
	}
}
