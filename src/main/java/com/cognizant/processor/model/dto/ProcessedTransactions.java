package com.cognizant.processor.model.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.cognizant.processor.model.dao.Transaction;

import lombok.Data;

@Data
public class ProcessedTransactions {

	private ProccessingResultType resultType;
	private List<ErrorTransactionData> errorRecords;

	public ProcessedTransactions() {
		this.errorRecords = new ArrayList<>();
	}

	public ProcessedTransactions(ProccessingResultType resultType) {
		this();
		this.resultType = resultType;
	}

	public void addErrorRecord(Transaction transaction) {
		ErrorTransactionData validatedTransaction = new ErrorTransactionData(transaction);
		errorRecords.add(validatedTransaction);
	}

	public void addErrorRecords(Collection<Transaction> transactions) {
		List<ErrorTransactionData> errors = transactions.stream().map(t -> new ErrorTransactionData(t))
				.collect(Collectors.toList());
		errorRecords.addAll(errors);
	}

}
