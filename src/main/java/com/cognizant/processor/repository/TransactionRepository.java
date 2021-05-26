package com.cognizant.processor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.processor.model.dao.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	List<Transaction> findAll();

	Optional<Transaction> findByReference(Long reference);
}
