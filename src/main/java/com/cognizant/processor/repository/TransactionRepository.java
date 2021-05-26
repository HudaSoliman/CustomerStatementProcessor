package com.cognizant.processor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;

import com.cognizant.processor.model.dao.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	List<Transaction> findAll(Example<Transaction> probe);

	List<Transaction> findAllByAccountNumber(String accountNumber);
	
	Optional<Transaction> findByReference(Long reference);
}
