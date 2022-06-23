package com.otto.ramarket.exception;

import org.springframework.http.HttpStatus;

public class ShopUnitRequestValidationException extends AbstractShopUnitException {

    public ShopUnitRequestValidationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
