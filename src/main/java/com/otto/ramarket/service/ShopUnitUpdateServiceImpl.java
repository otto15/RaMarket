package com.otto.ramarket.service;

import com.otto.ramarket.communication.ShopUnitStatisticUnit;
import com.otto.ramarket.entity.ShopUnit;
import com.otto.ramarket.entity.ShopUnitUpdate;
import com.otto.ramarket.exception.NoSuchShopUnitException;
import com.otto.ramarket.repository.ShopUnitRepository;
import com.otto.ramarket.repository.ShopUnitUpdateRepository;
import com.otto.ramarket.validation.ValidationExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShopUnitUpdateServiceImpl implements ShopUnitUpdateService {

    private final ShopUnitUpdateRepository shopUnitUpdateRepository;
    private final ShopUnitRepository shopUnitRepository;

    @Autowired
    public ShopUnitUpdateServiceImpl(ShopUnitUpdateRepository shopUnitUpdateRepository, ShopUnitRepository shopUnitRepository) {
        this.shopUnitUpdateRepository = shopUnitUpdateRepository;
        this.shopUnitRepository = shopUnitRepository;
    }

    @Override
    public List<ShopUnitStatisticUnit> getStatsForTheShopUnit(UUID id, Instant leftBorderDate, Instant rightBorderDate) {
        Optional<ShopUnit> shopUnitOptional = shopUnitRepository.findById(id);
        if (shopUnitOptional.isEmpty()) {
            throw new NoSuchShopUnitException(ValidationExceptionMessage.SHOP_UNIT_NOT_FOUND.getMessage() + id);
        }

        List<ShopUnitUpdate> shopUnitUpdates = shopUnitUpdateRepository
                .findAllByShopUnitIdAndUpdateDateGreaterThanEqualAndUpdateDateLessThan(id, leftBorderDate, rightBorderDate);

        return shopUnitUpdates.stream().map(this::getStatisticUnitFromUpdate)
                .collect(Collectors.toList());
    }

    @Override
    public void saveUpdate(ShopUnit shopUnit) {
        shopUnitUpdateRepository.save(getUpdateFromUnit(shopUnit));
    }

    public ShopUnitStatisticUnit getStatisticUnitFromUpdate(ShopUnitUpdate shopUnitUpdate) {
        ShopUnitStatisticUnit shopUnitStatisticUnit = new ShopUnitStatisticUnit();
        shopUnitStatisticUnit.setId(shopUnitUpdate.getShopUnit().getId());
        shopUnitStatisticUnit.setType(shopUnitUpdate.getShopUnit().getType());
        shopUnitStatisticUnit.setPrice(shopUnitUpdate.getPrice());
        shopUnitStatisticUnit.setName(shopUnitUpdate.getName());
        shopUnitStatisticUnit.setDate(shopUnitUpdate.getUpdateDate());
        shopUnitStatisticUnit.setParentId(shopUnitUpdate.getShopUnit().getParentId());

        return shopUnitStatisticUnit;
    }

    public ShopUnitUpdate getUpdateFromUnit(ShopUnit shopUnit) {
        ShopUnitUpdate shopUnitUpdate = new ShopUnitUpdate();
        shopUnitUpdate.setShopUnit(shopUnit);
        shopUnitUpdate.setUpdateDate(shopUnit.getDate());
        shopUnitUpdate.setName(shopUnit.getName());
        shopUnitUpdate.setPrice(shopUnit.getPrice());
        return shopUnitUpdate;
    }

}
