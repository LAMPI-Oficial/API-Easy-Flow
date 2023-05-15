package br.com.ifce.easyflow.service.daily.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String detail) {
        super(detail);
    }
}
