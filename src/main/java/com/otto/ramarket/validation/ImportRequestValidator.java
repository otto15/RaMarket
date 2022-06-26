package com.otto.ramarket.validation;

import com.otto.ramarket.communication.ShopUnitImport;
import com.otto.ramarket.communication.ShopUnitImportRequest;
import com.otto.ramarket.entity.ShopUnit;
import com.otto.ramarket.entity.ShopUnitType;
import com.otto.ramarket.exception.ShopUnitRequestValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ImportRequestValidator {

    public void validateImportRequest(ShopUnitImportRequest importRequest, Map<UUID, ShopUnit> shopUnitsFromDB,
                                      Map<UUID, ShopUnit> parentShopUnits) {
        Map<UUID, ShopUnitImport> shopUnitImports = importRequest.getItems()
                .stream()
                .collect(Collectors.toMap(ShopUnitImport::getId, Function.identity()));
        Set<UUID> metUUIDs = new HashSet<>();
        for (ShopUnitImport shopUnitImport : shopUnitImports.values()) {
            checkOnShopUnitTypeAndPriceCompliance(shopUnitImport);
            if (metUUIDs.contains(shopUnitImport.getId())) {
                throw new ShopUnitRequestValidationException(
                        ValidationExceptionMessage.ID_DUPLICATION.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
            if (shopUnitsFromDB.containsKey(shopUnitImport.getId())) {
                if (shopUnitImport.getType() != shopUnitsFromDB.get(shopUnitImport.getId()).getType()) {
                    throw new ShopUnitRequestValidationException(
                            ValidationExceptionMessage.TYPE_MISMATCH.getMessage(),
                            HttpStatus.BAD_REQUEST
                    );
                }
            }
            metUUIDs.add(shopUnitImport.getId());
            if (shopUnitImport.getParentId() != null) {
                checkOnParentExistenceAndTypeCorrectness(shopUnitImport, shopUnitImports, parentShopUnits);
            }
        }

    }

    public void checkOnParentExistenceAndTypeCorrectness(ShopUnitImport shopUnitImport,
                                                         Map<UUID, ShopUnitImport> shopUnitImports,
                                                         Map<UUID, ShopUnit> parentShopUnits) {
        if (parentShopUnits.containsKey(shopUnitImport.getParentId())) {
            if (parentShopUnits.get(shopUnitImport.getParentId()).getType() != ShopUnitType.CATEGORY) {
                throw new ShopUnitRequestValidationException(
                        ValidationExceptionMessage.WRONG_PARENT_TYPE.getMessage(),
                        HttpStatus.BAD_REQUEST
                );
            }
            return;
        }
        if (shopUnitImports.containsKey(shopUnitImport.getParentId())) {
            if (shopUnitImports.get(shopUnitImport.getParentId()).getType() != ShopUnitType.CATEGORY) {
                throw new ShopUnitRequestValidationException(
                        ValidationExceptionMessage.WRONG_PARENT_TYPE.getMessage(),
                        HttpStatus.BAD_REQUEST
                );
            }
            return;
        }
        throw new ShopUnitRequestValidationException(
                ValidationExceptionMessage.PARENT_DO_NOT_EXIST.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    public void checkOnShopUnitTypeAndPriceCompliance(ShopUnitImport shopUnitImport) {
        if (shopUnitImport.getType() == ShopUnitType.CATEGORY && shopUnitImport.getPrice() != null) {
            throw new ShopUnitRequestValidationException(
                    ValidationExceptionMessage.CATEGORY_AND_PRICE_IS_NOT_NULL.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        if (shopUnitImport.getType() == ShopUnitType.OFFER) {
            if (shopUnitImport.getPrice() == null) {
                throw new ShopUnitRequestValidationException(
                        ValidationExceptionMessage.OFFER_AND_PRICE_IS_NULL.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
            if (shopUnitImport.getPrice() < 0) {
                throw new ShopUnitRequestValidationException(
                        ValidationExceptionMessage.OFFER_AND_PRICE_IS_LESS_THAN_ZERO.getMessage(),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }


}
