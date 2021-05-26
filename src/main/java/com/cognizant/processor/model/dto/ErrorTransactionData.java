package com.cognizant.processor.model.dto;

import com.cognizant.processor.model.dao.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTransactionData {

	private Long reference;
	private String accountNumber;

	public ErrorTransactionData(Transaction transaction) {
		setReference(transaction.getReference());
		setAccountNumber(transaction.getAccountNumber());
	}

}
