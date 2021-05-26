package com.cognizant.processor.service.account;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.processor.model.dao.Account;
import com.cognizant.processor.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepository;

	@Override
	public Account findByAccountNumber(String accountNumber) {
		Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
		if (account.isPresent()) {
			return account.get();
		} else {
			return null;
		}
	}

	@Override
	public Account save(Account account) {
	
		return accountRepository.save(account);
	}


}
