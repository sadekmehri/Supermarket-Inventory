package com.example.project.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NumericValidator implements ConstraintValidator<Numeric, Integer> {

    @Override
    public void initialize(Numeric constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        String pattern = "^(0|[1-9][0-9]*)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value.toString());
        return m.matches();
    }

}
