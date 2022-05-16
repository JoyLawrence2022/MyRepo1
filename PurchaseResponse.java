package com.sportyshoes.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseResponse {
	private final String orderTrackingNumber;
}
