package com.sportyshoes.repositories;

import com.sportyshoes.entities.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long>{
	
}
