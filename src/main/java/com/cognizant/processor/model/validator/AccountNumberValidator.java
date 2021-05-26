package com.cognizant.processor.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.IBANValidator;

public class AccountNumberValidator implements 
ConstraintValidator<AccountNumberConstraint, String> {

  @Override
  public void initialize(AccountNumberConstraint contactNumber) {
  }

  @Override
  public boolean isValid(String accountNumberField,
    ConstraintValidatorContext cxt) {
      return IBANValidator.getInstance().isValid(accountNumberField);
  }

}