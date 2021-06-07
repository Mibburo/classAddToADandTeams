package com.uagean.eIDEuSmartClass.ad.teams.controller.validator;

import com.uagean.eIDEuSmartClass.ad.teams.model.EmailForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class EmailValidator implements Validator {

    private static final String INVALID_EMAIL = "invalid.email";

    Pattern mailPattern = Pattern
            .compile("([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{1,})");

    @Override
    public boolean supports(Class<?> clazz) {
        return EmailForm.class==clazz;
    }

    @Override
    public void validate(Object arg0, Errors errors) {

        EmailForm emailForm = (EmailForm) arg0;

        if (!mailPattern.matcher(emailForm.getEmail()).matches()) {
            errors.rejectValue("email", INVALID_EMAIL);
        }

        return;

    }

}
