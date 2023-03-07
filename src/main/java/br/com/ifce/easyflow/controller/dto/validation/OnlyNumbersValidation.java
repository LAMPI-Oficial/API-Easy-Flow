package br.com.ifce.easyflow.controller.dto.validation;

import br.com.ifce.easyflow.controller.dto.validation.constraints.OnlyNumbers;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyNumbersValidation implements ConstraintValidator<OnlyNumbers, String> {
    @Override
    public void initialize(OnlyNumbers constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String value = s == null ? "" : s;

        return value.matches("^[0-9]+$");
    }
}
