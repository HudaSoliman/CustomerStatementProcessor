package com.cognizant.processor.service.transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.processor.model.dao.Transaction;
import com.cognizant.processor.model.dto.ProccessingResultType;
import com.cognizant.processor.model.dto.ProcessedTransactions;
import com.cognizant.processor.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public Transaction findByReferenceNumber(Long reference) {
		Optional<Transaction> transaction = transactionRepository.findByReference(reference);
		if (transaction.isPresent()) {
			return transaction.get();
		}
		return null;
	}

	@Transactional
	@Override
	public ProcessedTransactions processTransactions(List<Transaction> inputTransactions) {
		ProcessedTransactions result = new ProcessedTransactions();
		// get transactions with invalid balance
		List<Transaction> invalidBalanceTransactions = getInvalidTransactions(inputTransactions);

		Set<Transaction> duplicateTransactions = getDuplicateTransactions(inputTransactions);

		if (invalidBalanceTransactions.isEmpty() && duplicateTransactions.isEmpty()) {

			result.setResultType(ProccessingResultType.SUCCESSFUL);
			// save transactions..
			inputTransactions.stream().forEach(transactionRepository::save);

		} else if (invalidBalanceTransactions.isEmpty() && !duplicateTransactions.isEmpty()) {

			result.setResultType(ProccessingResultType.DUPLICATE_REFERENCE);
			result.addErrorRecords(duplicateTransactions);
		} else if (!invalidBalanceTransactions.isEmpty() && duplicateTransactions.isEmpty()) {

			result.setResultType(ProccessingResultType.INCORRECT_END_BALANCE);
			result.addErrorRecords(invalidBalanceTransactions);
		} else if (!invalidBalanceTransactions.isEmpty() && !duplicateTransactions.isEmpty()) {

			result.setResultType(ProccessingResultType.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
			result.addErrorRecords(invalidBalanceTransactions);
			result.addErrorRecords(duplicateTransactions);
		}

		return result;

	}

	private List<Transaction> getInvalidTransactions(List<Transaction> inputTransactions) {
		return inputTransactions.stream().filter(t -> !t.isValidBalance()).collect(Collectors.toList());
	}

	private Set<Transaction> getDuplicateTransactions(List<Transaction> inputTransactions) {
		// get transactions with duplicate within the given list
		Set<Transaction> duplicateTransactions = findDuplicatesWithinList(inputTransactions);

		// find duplicates of transactions with the database
		Set<Transaction> duplicateTransactionsWithDatabase = inputTransactions.stream()
				.filter(this::isExistingTransaction).collect(Collectors.toSet());

		duplicateTransactions.addAll(duplicateTransactionsWithDatabase);

		return duplicateTransactions;

	}

	private boolean isExistingTransaction(Transaction transaction) {
		Transaction t = this.findByReferenceNumber(transaction.getReference());
		return !Objects.isNull(t);
	}

	private Set<Transaction> findDuplicatesWithinList(List<Transaction> list) {
		Set<Transaction> duplicatedRemovedSet = new HashSet<>();
		return list.stream().filter(n -> !duplicatedRemovedSet.add(n)).collect(Collectors.toSet());
	}

}
