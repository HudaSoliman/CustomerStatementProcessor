package com.cognizant.processor.model.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.apache.commons.validator.routines.IBANValidator;

import com.cognizant.processor.model.validator.AccountNumberConstraint;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	private  Long reference;
	@AccountNumberConstraint
	private  String accountNumber;
	@NotBlank(message = "Invalid empty balance")
	@Positive
	private  Double startBalance;
	@NotBlank
	private  Double mutation;
	private  String description;
	@NotBlank
	private  Double endBalance;


	@JsonCreator
	public Transaction(Long reference, String accountNumber, Double startBalance, Double mutation, String description,
			Double endBalance) {
		super();
		this.reference = reference;
		this.accountNumber = accountNumber;
		this.startBalance = startBalance;
		this.mutation = mutation;
		this.description = description;
		this.endBalance = endBalance;
	}

	public boolean isValidBalance() {
		int result = ((Double) (getStartBalance() + getMutation())).compareTo(getEndBalance());
		return result == 0;
	}
	
	public boolean isAccountNumber() {
		IBANValidator.getInstance().isValid(accountNumber);
		int result = ((Double) (getStartBalance() + getMutation())).compareTo(getEndBalance());
		return result == 0;
	}
	
	

}
