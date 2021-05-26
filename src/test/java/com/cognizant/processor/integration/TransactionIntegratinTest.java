package com.cognizant.processor.integration;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.processor.model.dao.Transaction;
import com.cognizant.processor.repository.TransactionRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionIntegratinTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	public void whenFindByReference_thenReturnTransaction() {
		// given
		Transaction transaction = new Transaction(16530L, "NL93ABNA0585619023", 100D, -88D, "testing", 92D);
		entityManager.persist(transaction);
		entityManager.flush();

		// when
		Optional<Transaction> found = transactionRepository.findByReference(16530L);

		// then
		Assert.assertTrue(found.isPresent());
		Assert.assertEquals(found.get(), transaction);

	}
	
	@Test
	public void whenFindAll_thenReturnAllTransactions() {
		// given
		Transaction transaction = new Transaction(16343530L, "NL93ABNA0585619023", 100D, -88D, "testing", 92D);
		entityManager.persist(transaction);
		entityManager.flush();

		// when
		 List<Transaction> found = transactionRepository.findAll();

		// then
		Assert.assertFalse(found.isEmpty());
		Assert.assertTrue(found.contains(transaction));

	}

}