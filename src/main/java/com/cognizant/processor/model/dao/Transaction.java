package com.cognizant.processor.model.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	// TODO: add IBANValidator
	private  String accountNumber;
	private  Double startBalance;
	private  Double mutation;
	private  String description;
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

}
