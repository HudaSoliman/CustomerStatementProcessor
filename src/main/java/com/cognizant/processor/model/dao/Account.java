package com.cognizant.processor.model.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account")
public class Account {

	// TODO: add IBANValidator
	@Id
	private String accountNumber;
	
	
	private double balance;

}
