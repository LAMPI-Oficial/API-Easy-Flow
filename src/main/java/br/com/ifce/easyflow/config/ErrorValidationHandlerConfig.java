package br.com.ifce.easyflow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorValidationHandlerConfig {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorDTO> handle(MethodArgumentNotValidException exception) {
        List<ErrorDTO> errors = new ArrayList<>();

        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(e -> {
                    errors.add(new ErrorDTO(
                            e.getField(),
                            messageSource.getMessage(e, LocaleContextHolder.getLocale()
                            )
                        )
                    );

        });

        return errors;
    }

}
