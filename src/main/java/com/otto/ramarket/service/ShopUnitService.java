package com.otto.ramarket.service;

import com.otto.ramarket.communication.ShopUnitImportRequest;
import com.otto.ramarket.communication.ShopUnitStatisticUnit;
import com.otto.ramarket.entity.ShopUnit;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ShopUnitService {

    void executeImport(ShopUnitImportRequest shopUnitImportRequest);
    void deleteShopUnitById(UUID id);
    ShopUnit getShopUnitById(UUID id);
    List<ShopUnitStatisticUnit> getShopUnitsUpdatedInLast24HoursFromGivenDate(Instant date);

}
