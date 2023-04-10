package br.com.ifce.easyflow.config;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ValidationExceptionDetails extends ProblemDetails {

    private final String fields;

    private final String fieldsMessage;

}
