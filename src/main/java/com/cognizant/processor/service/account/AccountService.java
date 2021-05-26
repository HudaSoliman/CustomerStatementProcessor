package com.cognizant.processor.service.account;

import org.springframework.stereotype.Service;

import com.cognizant.processor.model.dao.Account;

@Service 
public interface AccountService {

	public Account findByAccountNumber(String accountNumber);

	public Account save(Account account);

}
