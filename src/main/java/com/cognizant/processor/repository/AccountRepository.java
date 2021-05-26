package com.cognizant.processor.repository;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;

import com.cognizant.processor.model.dao.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
	
	Iterable<Account> findAll(Example<Account> probe);

	Optional<Account> findByAccountNumber(String accountNumber);
}
