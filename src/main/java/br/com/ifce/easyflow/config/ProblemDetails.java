package br.com.ifce.easyflow.config;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter @SuperBuilder
public class ProblemDetails {
    private String title;

    private String detail;

    private int status;

    private Instant timestamp;

}
