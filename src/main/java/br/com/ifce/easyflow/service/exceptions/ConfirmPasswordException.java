package br.com.ifce.easyflow.service.daily.exceptions;

public class ConfirmPasswordException extends RuntimeException {
    public ConfirmPasswordException(String message) {
        super(message);
    }
}
