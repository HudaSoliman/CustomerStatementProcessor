package com.cognizant.processor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.processor.model.dao.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
	
	List<Account> findAll(Example<Account> probe);

	Optional<Account> findByAccountNumber(String accountNumber);
}
