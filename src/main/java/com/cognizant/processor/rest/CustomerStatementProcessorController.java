package com.cognizant.processor.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.processor.model.dao.Transaction;
import com.cognizant.processor.model.dto.ValidatedTransactions;
import com.cognizant.processor.model.dto.ValidationResultType;
import com.cognizant.processor.service.transaction.TransactionService;

@RestController
@ExposesResourceFor(Transaction.class)
@RequestMapping("/api/transactions")
public class CustomerStatementProcessorController {

	@Autowired
	TransactionService transactionService;

	@Transactional
	@PostMapping(path = "/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ValidatedTransactions> processData(@RequestBody List<Transaction> inputTransactions) {

		try {
			ValidatedTransactions result = transactionService.processTransactions(inputTransactions);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			ValidatedTransactions result = new ValidatedTransactions(ValidationResultType.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}

	@ExceptionHandler
	public ResponseEntity<ValidatedTransactions> handle(HttpMessageConversionException e) {
		ValidatedTransactions result = new ValidatedTransactions(ValidationResultType.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}
	
}
