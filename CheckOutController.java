package com.sportyshoes.controllers;

import com.sportyshoes.entities.PurchaseDto;
import com.sportyshoes.entities.PurchaseResponse;
import com.sportyshoes.services.CheckOutService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/checkout")
public class CheckOutController {
	@Autowired
	private CheckOutService checkOutService;
	@PostMapping("/purchase")
	public PurchaseResponse placeOrder(@RequestBody PurchaseDto PurchaseDto) {
		PurchaseResponse response = checkOutService.placeOrder(PurchaseDto);
		return response;
	}
}
