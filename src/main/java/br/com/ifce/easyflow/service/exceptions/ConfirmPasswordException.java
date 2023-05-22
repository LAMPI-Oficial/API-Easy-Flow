package br.com.ifce.easyflow.service.exceptions;

public class ConfirmPasswordException extends RuntimeException {
    public ConfirmPasswordException(String message) {
        super(message);
    }
}
