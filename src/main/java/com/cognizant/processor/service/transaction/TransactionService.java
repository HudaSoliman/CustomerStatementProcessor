package com.cognizant.processor.service.transaction;

import java.util.List;

import com.cognizant.processor.model.dao.Transaction;
import com.cognizant.processor.model.dto.ValidatedTransactions;

public interface TransactionService {

	public Transaction findByReferenceNumber(Long referenceNumber);

	public ValidatedTransactions processTransactions(List<Transaction> inputTransactions);

}
