package com.otto.ramarket.exception;

import org.springframework.http.HttpStatus;

public class NoSuchShopUnitException extends AbstractShopUnitException {

    public NoSuchShopUnitException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
