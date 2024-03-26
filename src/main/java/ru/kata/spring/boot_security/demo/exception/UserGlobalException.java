package ru.kata.spring.boot_security.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class UserGlobalException {

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<UserIncorectData> handlerValidationException(UserValidationException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        UserIncorectData data = new UserIncorectData();
        for (FieldError fieldError : fieldErrors) {
            data.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorectData> handlerException(Exception exception) {
        UserIncorectData data = new UserIncorectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data,HttpStatus.BAD_REQUEST);
    }

}
