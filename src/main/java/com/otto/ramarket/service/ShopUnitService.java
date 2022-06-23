package com.otto.ramarket.service;

import com.otto.ramarket.communication.ShopUnitImport;
import com.otto.ramarket.communication.ShopUnitImportRequest;
import com.otto.ramarket.entity.ShopUnit;

import java.time.ZonedDateTime;
import java.util.List;

public interface ShopUnitService {

    void mergeShopUnit(ShopUnit shopUnit);
    void mergeAllShopUnits(List<ShopUnit> shopUnits);
    void mergeAllShopUnitsWithAddingUpdateDate(ShopUnitImportRequest shopUnitImportRequest);
    void processImport(ShopUnitImportRequest shopUnitImportRequest);

}
