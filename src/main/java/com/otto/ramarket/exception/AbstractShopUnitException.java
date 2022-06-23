package com.otto.ramarket.exception;

import org.springframework.http.HttpStatus;

public class AbstractShopUnitException extends RuntimeException {

    private final HttpStatus httpStatus;

    public AbstractShopUnitException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
