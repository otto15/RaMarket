package com.otto.ramarket.validation;

import com.otto.ramarket.communication.ShopUnitImport;
import com.otto.ramarket.communication.ShopUnitImportRequest;
import com.otto.ramarket.entity.ShopUnit;
import com.otto.ramarket.entity.ShopUnitType;
import com.otto.ramarket.exception.ShopUnitRequestValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ImportRequestValidator {

    public void validateImportRequest(ShopUnitImportRequest importRequest, Map<UUID, ShopUnit> shopUnitMap) {
        List<ShopUnitImport> shopUnitImports = importRequest.getItems();
        for (ShopUnitImport shopUnitImport : shopUnitImports) {
            checkOnShopUnitTypeAndPriceCompliance(shopUnitImport);

        }
    }

    public void checkOnShopUnitTypeAndPriceCompliance(ShopUnitImport shopUnitImport) {
        if (shopUnitImport.getType() == ShopUnitType.CATEGORY && shopUnitImport.getPrice() != null) {
            throw new ShopUnitRequestValidationException("Category and price != null", HttpStatus.BAD_REQUEST);
        }
        if (shopUnitImport.getType() == ShopUnitType.OFFER) {
            if (shopUnitImport.getPrice() == null) {
                throw new ShopUnitRequestValidationException("Offer and price = null", HttpStatus.BAD_REQUEST);
            }
            if (shopUnitImport.getPrice() < 0) {
                throw new ShopUnitRequestValidationException("Offer and price < 0", HttpStatus.BAD_REQUEST);
            }
        }
    }



}
