package com.sportyshoes.entities;

import java.util.Set;

import lombok.Data;

@Data
public class PurchaseDto {
	private Customer customer;
	private Address shippingAddress;
	private Address billingAddress;
	private Order order;
	private Set<OrderItems> orderItems;
}
