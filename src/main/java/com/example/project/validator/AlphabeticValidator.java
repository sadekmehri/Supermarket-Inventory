package com.example.project.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AlphabeticValidator implements ConstraintValidator<Alphabetic, String> {

    @Override
    public void initialize(Alphabetic constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String pattern = "^([a-zA-Z]+\\s)*[a-zA-Z]+$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);
        return m.matches();
    }

}
