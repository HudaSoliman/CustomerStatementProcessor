package com.cognizant.processor.integration;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.processor.CustomerStatementProcessorApplication;
import com.cognizant.processor.service.transaction.TransactionService;

import utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CustomerStatementProcessorApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class CustomerStatementProcessorControllerIntegrationTest {

	private TestUtils testUtils;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private TransactionService transactionService;

	@Before
	public void setUp() {
		testUtils = new TestUtils();
	}

	@Test
	@Transactional(propagation = Propagation.NESTED)
	public void givenNormalRecords_whenProcessData_thenStatus200() throws Exception {

		JSONArray json = testUtils.loadJsonFile("correct_transactions.json");

		mvc.perform(
				post("/api/transactions/process").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultType", is("SUCCESSFUL")));

		// testing records are saved in db

		Assert.assertNotNull(transactionService.findByReferenceNumber(130498L));
		Assert.assertNotNull(transactionService.findByReferenceNumber(167875L));
		Assert.assertNotNull(transactionService.findByReferenceNumber(147674L));
	}

	@Test
	public void givenDuplicateRecords_whenProcessData_thenStatus200() throws Exception {

		JSONArray json = testUtils.loadJsonFile("duplicate_records.json");

		mvc.perform(
				post("/api/transactions/process").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultType", is("DUPLICATE_REFERENCE")));

		// testing no records saved in db
		Assert.assertNull(transactionService.findByReferenceNumber(130498L));
		Assert.assertNull(transactionService.findByReferenceNumber(167875L));
		Assert.assertNull(transactionService.findByReferenceNumber(147674L));

	}

	@Test
	public void givenDuplicateAndInvalindEndBalanceRecords_whenProcessData_thenStatus200() throws Exception {

		JSONArray json = testUtils.loadJsonFile("duplicate_and_invalid_records.json");

		mvc.perform(
				post("/api/transactions/process").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultType", is("INCORRECT_END_BALANCE")));

		// testing no records saved in db
		Assert.assertNull(transactionService.findByReferenceNumber(130498L));
		Assert.assertNull(transactionService.findByReferenceNumber(167875L));
		Assert.assertNull(transactionService.findByReferenceNumber(147674L));
		Assert.assertNull(transactionService.findByReferenceNumber(135607L));
		Assert.assertNull(transactionService.findByReferenceNumber(169639L));
		Assert.assertNull(transactionService.findByReferenceNumber(105549L));
		Assert.assertNull(transactionService.findByReferenceNumber(150438L));

	}

	@Test
	public void givenInvalindEndBalanceRecords_whenProcessData_thenStatus200() throws Exception {

		JSONArray json = testUtils.loadJsonFile("invalid_records.json");

		mvc.perform(
				post("/api/transactions/process").contentType(MediaType.APPLICATION_JSON).content(json.toJSONString()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.resultType", is("INCORRECT_END_BALANCE")));
		
		
		Assert.assertNull(transactionService.findByReferenceNumber(130498L));
		Assert.assertNull(transactionService.findByReferenceNumber(147674L));
		Assert.assertNull(transactionService.findByReferenceNumber(135607L));
		
	}

}
