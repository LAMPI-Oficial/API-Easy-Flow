package br.com.ifce.easyflow.controller.dto.validation.constraints;

import br.com.ifce.easyflow.controller.dto.validation.OnlyNumbersValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OnlyNumbersValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyNumbers {
    String message() default "This field is invalid, it can only contain numbers.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
