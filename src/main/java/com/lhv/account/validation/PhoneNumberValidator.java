package com.lhv.account.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNr, ConstraintValidatorContext context) {
        if (phoneNr == null || phoneNr.isBlank()) return true;

        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNr, "");
            return phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }
}