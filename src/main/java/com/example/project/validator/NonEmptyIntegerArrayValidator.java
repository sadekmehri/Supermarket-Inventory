package com.example.project.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NonEmptyIntegerArrayValidator implements ConstraintValidator<NonEmptyIntegerArray, int[]> {
    @Override
    public void initialize(NonEmptyIntegerArray constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(int[] value, ConstraintValidatorContext context) {
        return value.length != 0;
    }
}
