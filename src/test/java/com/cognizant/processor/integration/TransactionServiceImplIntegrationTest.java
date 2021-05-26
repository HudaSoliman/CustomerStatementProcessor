package com.cognizant.processor.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.processor.model.dao.Account;
import com.cognizant.processor.model.dao.Transaction;
import com.cognizant.processor.model.dto.ErrorTransactionData;
import com.cognizant.processor.model.dto.ProcessedTransactions;
import com.cognizant.processor.model.dto.ProccessingResultType;
import com.cognizant.processor.repository.AccountRepository;
import com.cognizant.processor.repository.TransactionRepository;
import com.cognizant.processor.service.account.AccountService;
import com.cognizant.processor.service.transaction.TransactionService;
import com.cognizant.processor.service.transaction.TransactionServiceImpl;

@RunWith(SpringRunner.class)
public class TransactionServiceImplIntegrationTest {

	@TestConfiguration
	static class TransactionServiceImplTestContextConfiguration {

		@Bean
		public TransactionService transactionService() {
			return new TransactionServiceImpl();
		}
	}

	@Autowired
	private TransactionService transactionService;

	@MockBean
	private AccountService accountService;
	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	@Before
	public void setUp() {
		Account alex = new Account("NL93ABNA0585619023", 100L);
		Transaction alexTransaction = new Transaction(147674L, alex.getAccountNumber(), 100D, -8D, "testing", 92D);

		Mockito.doReturn(Optional.of(alexTransaction)).when(transactionRepository).findByReference(147674L);
	}

	@Test
	public void whenValidReference_thenTransactionShouldBeFound() {
		Transaction transaction = transactionService.findByReferenceNumber(147674L);

		Assert.assertEquals(Long.valueOf(147674L), transaction.getReference());
	}

	@Test
	public void whenValidTransactions_thenValidationResultShouldBeSuccessful() {

		Transaction transaction1 = new Transaction(148L, "NL93ABNA0585619023", 100D, -8D, "testing", 92D);
		Transaction transaction2 = new Transaction(149L, "NL93ABNA0585619043", 200D, -9D, "testing", 191D);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		transactions.add(transaction2);

		ProcessedTransactions result = transactionService.processTransactions(transactions);
		Assert.assertEquals(ProccessingResultType.SUCCESSFUL, result.getResultType());
		Assert.assertTrue(result.getErrorRecords().isEmpty());
	}

	@Test
	public void whenDuplicateTransactions_thenValidationResultShouldBeDuplicate() {

		Transaction transaction1 = new Transaction(150L, "NL93ABNA0585619023", 100D, -8D, "testing", 92D);
		Transaction transaction2 = new Transaction(150L, "NL93ABNA0585619023", 100D, -8D, "testing", 92D);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		transactions.add(transaction2);

		ProcessedTransactions result = transactionService.processTransactions(transactions);
		Assert.assertEquals(ProccessingResultType.DUPLICATE_REFERENCE, result.getResultType());
		Assert.assertEquals(1, result.getErrorRecords().size());

		List<ErrorTransactionData> expectedResults = new ArrayList<>();
		expectedResults.add(new ErrorTransactionData(transaction1));

		Assert.assertEquals(expectedResults, result.getErrorRecords());
	}

	@Test
	public void whenInvalidTransactions_thenValidationResultShouldBeInvalid() {

		Transaction transaction1 = new Transaction(158L, "NL93ABNA0585619023", 100D, -88D, "testing", 92D);
		Transaction transaction2 = new Transaction(159L, "NL93ABNA058599623", 100D, -8D, "testing", 92D);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		transactions.add(transaction2);

		ProcessedTransactions result = transactionService.processTransactions(transactions);
		Assert.assertEquals(ProccessingResultType.INCORRECT_END_BALANCE, result.getResultType());
		Assert.assertEquals(1, result.getErrorRecords().size());

		List<ErrorTransactionData> expectedResults = new ArrayList<>();
		expectedResults.add(new ErrorTransactionData(transaction1));

		Assert.assertEquals(expectedResults, result.getErrorRecords());
	}

	@Test
	public void whenInvalidAndDuplicateTransactions_thenValidationResultShouldBeInvalidAndDuplicate() {

		Transaction transaction1 = new Transaction(160L, "NL93ABNA0585619023", 100D, -88D, "testing", 92D);
		Transaction transaction2 = new Transaction(170L, "NL93ABNA058599623", 100D, -8D, "testing", 92D);
		Transaction transaction3 = new Transaction(170L, "NL93ABNA058599623", 100D, -8D, "testing", 92D);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		transactions.add(transaction2);
		transactions.add(transaction3);

		ProcessedTransactions result = transactionService.processTransactions(transactions);
		Assert.assertEquals(ProccessingResultType.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, result.getResultType());
		Assert.assertEquals(2, result.getErrorRecords().size());

		List<ErrorTransactionData> expectedResults = new ArrayList<>();
		expectedResults.add(new ErrorTransactionData(transaction1));
		expectedResults.add(new ErrorTransactionData(transaction2));

		Assert.assertEquals(expectedResults, result.getErrorRecords());
	}

}