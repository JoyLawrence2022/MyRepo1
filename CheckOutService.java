package com.sportyshoes.services;

import java.util.Set;
import java.util.UUID;

import com.sportyshoes.entities.Customer;
import com.sportyshoes.entities.Order;
import com.sportyshoes.entities.OrderItems;
import com.sportyshoes.entities.PurchaseDto;
import com.sportyshoes.entities.PurchaseResponse;
import com.sportyshoes.repositories.CustomerRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckOutService {
	@Autowired
	private CustomerRepo customerRepo;
	public PurchaseResponse placeOrder(PurchaseDto purchaseDto) {
		Order order = purchaseDto.getOrder();
		String orderTrackingNumber = generateUniqueTrackingNumber();
		order.setOrderTrackingNumber(orderTrackingNumber);
		Set<OrderItems> orderItems = purchaseDto.getOrderItems();
		orderItems.forEach(item -> order.add(item));
		//order.setBillingAddress(purchaseDto.getBillingAddress());
		order.setShippingAddress(purchaseDto.getShippingAddress());
		
		Customer customer = purchaseDto.getCustomer();
		customer.add(order);
		customerRepo.save(customer);
		return new PurchaseResponse(orderTrackingNumber);
		
	}
	private String generateUniqueTrackingNumber() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}
}
