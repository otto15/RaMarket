package com.otto.ramarket.exception.handling;

import com.otto.ramarket.communication.Error;
import com.otto.ramarket.exception.AbstractShopUnitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ShopUnitGlobalExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "Validation Failed";

    @ExceptionHandler
    public ResponseEntity<Error> handleException(Exception exception) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), DEFAULT_ERROR_MESSAGE);
        exception.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleException(AbstractShopUnitException shopUnitException) {
        HttpStatus httpStatus = shopUnitException.getHttpStatus();
        Error error = new Error(httpStatus.value(), shopUnitException.getMessage());

        return new ResponseEntity<>(error, httpStatus);
    }

}
