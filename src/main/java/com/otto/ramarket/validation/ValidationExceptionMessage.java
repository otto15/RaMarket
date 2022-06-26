package com.otto.ramarket.validation;

public enum ValidationExceptionMessage {

    CATEGORY_AND_PRICE_IS_NOT_NULL("Category's price must be null"),
    OFFER_AND_PRICE_IS_NULL("Offer's price must not be null"),
    OFFER_AND_PRICE_IS_LESS_THAN_ZERO("Offer's price must be equal or greater than zero"),
    ID_DUPLICATION("Id duplication error"),
    TYPE_MISMATCH("Changing the type is not allowed"),
    WRONG_PARENT_TYPE("Only CATEGORY can be parent"),
    PARENT_DO_NOT_EXIST("No parent found with such parentId"),
    SHOP_UNIT_NOT_FOUND("No such shop unit with id ");

    private final String message;

    ValidationExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
