package com.example.project.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;


public class TodayDateValidator implements ConstraintValidator<TodayDate, LocalDate> {

    @Override
    public void initialize(TodayDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return LocalDate.now().isAfter(value);
    }
}
