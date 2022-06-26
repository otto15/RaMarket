package com.otto.ramarket.service;

import com.otto.ramarket.communication.ShopUnitStatisticUnit;
import com.otto.ramarket.entity.ShopUnit;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ShopUnitUpdateService {

    List<ShopUnitStatisticUnit> getStatsForTheShopUnit(UUID id, Instant dateStart, Instant dateEnd);

    void saveUpdate(ShopUnit shopUnit);
}
