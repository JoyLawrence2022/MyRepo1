package com.sportyshoes.services;

import java.util.List;

import com.sportyshoes.entities.Order;
import com.sportyshoes.repositories.OrderRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	@Autowired
	private OrderRepo orderRepository;
	
	public List<Order> getOrders(){
		return orderRepository.findAll();
	}
}
