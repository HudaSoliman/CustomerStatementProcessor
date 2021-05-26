package com.cognizant.processor.service.account;

import com.cognizant.processor.model.dao.Account;

public interface AccountService {

	public Account findByAccountNumber(String accountNumber);

	public Account save(Account account);

}
