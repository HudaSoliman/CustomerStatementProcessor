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

import com.cognizant.processor.model.dao.Account;
import com.cognizant.processor.model.dao.Transaction;
import com.cognizant.processor.model.dto.ValidatedTransactions;
import com.cognizant.processor.model.dto.ValidationResultType;
import com.cognizant.processor.repository.TransactionRepository;
import com.cognizant.processor.service.account.AccountService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	AccountService accountService;

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
	public ValidatedTransactions processTransactions(List<Transaction> inputTransactions) {
		ValidatedTransactions result = new ValidatedTransactions();
		// get transactions with invalid balance
		List<Transaction> invalidBalanceTransactions = inputTransactions.stream().filter(t -> !t.isValidBalance())
				.collect(Collectors.toList());

		// get transactions with duplicate within the given list
		Set<Transaction> duplicateTransactions = findDuplicatesWithinList(inputTransactions);

		// find duplicates of transactions with the database
		Set<Transaction> duplicateTransactionsWithDatabase = inputTransactions.stream()
				.filter(this::isExistingTransaction).collect(Collectors.toSet());

		duplicateTransactions.addAll(duplicateTransactionsWithDatabase);

		if (invalidBalanceTransactions.isEmpty() && duplicateTransactions.isEmpty()) {

			result.setResultType(ValidationResultType.SUCCESSFUL);

			// save transactions..
			inputTransactions.stream().forEach(this::saveTransaction);

		} else if (invalidBalanceTransactions.isEmpty() && !duplicateTransactions.isEmpty()) {

			result.setResultType(ValidationResultType.DUPLICATE_REFERENCE);
			result.addErrorRecords(duplicateTransactions);
		} else if (!invalidBalanceTransactions.isEmpty() && duplicateTransactions.isEmpty()) {

			result.setResultType(ValidationResultType.INCORRECT_END_BALANCE);
			result.addErrorRecords(invalidBalanceTransactions);
		} else if (!invalidBalanceTransactions.isEmpty() && !duplicateTransactions.isEmpty()) {

			result.setResultType(ValidationResultType.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
			result.addErrorRecords(invalidBalanceTransactions);
			result.addErrorRecords(duplicateTransactions);
		}

		return result;

	}

	private void saveTransaction(Transaction t) {
		// saving account first..
		String accountNumber = t.getAccountNumber();
		Account account = accountService.findByAccountNumber(accountNumber);

		if (Objects.isNull(account)) {
			account = new Account(accountNumber, t.getEndBalance());
		} else {
			account.setBalance(t.getEndBalance());
		}
		accountService.save(account);

		// saving transaction
		transactionRepository.save(t);

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
