package com.example.project.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = NonEmptyIntegerArrayValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented

public @interface NonEmptyIntegerArray {
    String message() default "Empty array is not allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
