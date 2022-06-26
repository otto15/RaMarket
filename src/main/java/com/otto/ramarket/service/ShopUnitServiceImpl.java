package com.otto.ramarket.service;

import com.otto.ramarket.communication.ShopUnitImport;
import com.otto.ramarket.communication.ShopUnitImportRequest;
import com.otto.ramarket.communication.ShopUnitStatisticUnit;
import com.otto.ramarket.entity.ShopUnit;
import com.otto.ramarket.exception.NoSuchShopUnitException;
import com.otto.ramarket.repository.ShopUnitRepository;
import com.otto.ramarket.validation.ImportRequestValidator;
import com.otto.ramarket.validation.ValidationExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShopUnitServiceImpl implements ShopUnitService {

    private static final long ONE_DAY_IN_SECONDS = 86400;
    private final ShopUnitRepository shopUnitRepository;
    private final ImportRequestValidator importRequestValidator;
    private final ShopUnitUpdateService shopUnitUpdateService;

    @Autowired
    public ShopUnitServiceImpl(ShopUnitRepository shopUnitRepository, ImportRequestValidator importRequestValidator,
                               ShopUnitUpdateService shopUnitUpdateService) {
        this.shopUnitRepository = shopUnitRepository;
        this.importRequestValidator = importRequestValidator;
        this.shopUnitUpdateService = shopUnitUpdateService;
    }

    @Override
    public void executeImport(ShopUnitImportRequest shopUnitImportRequest) {
        Map<UUID, ShopUnit> shopUnitsFromDB = getAllShopUnitsById(shopUnitImportRequest.getItems());
        Map<UUID, ShopUnit> parentShopUnitsFromDB = getAllParentShopUnitsByParentId(shopUnitImportRequest.getItems());
        importRequestValidator.validateImportRequest(shopUnitImportRequest, shopUnitsFromDB, parentShopUnitsFromDB);

        mergeAllShopUnitImports(shopUnitImportRequest, shopUnitsFromDB, parentShopUnitsFromDB);
    }

    @Override
    public void deleteShopUnitById(UUID id) {
        try {
            shopUnitRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchShopUnitException(ValidationExceptionMessage.SHOP_UNIT_NOT_FOUND.getMessage() + id);
        }
    }

    @Override
    public ShopUnit getShopUnitById(UUID id) {
        Optional<ShopUnit> shopUnitOptional = shopUnitRepository.findById(id);
        if (shopUnitOptional.isEmpty()) {
            throw new NoSuchShopUnitException(ValidationExceptionMessage.SHOP_UNIT_NOT_FOUND.getMessage() + id);
        }
        return shopUnitOptional.get();
    }

    @Override
    public List<ShopUnitStatisticUnit> getShopUnitsUpdatedInLast24HoursFromGivenDate(Instant date) {
        List<ShopUnit> shopUnits = shopUnitRepository.findAllByDateIsBetween(date.minusSeconds(ONE_DAY_IN_SECONDS),
                date);

        return getStatisticUnitsFromShopUnits(shopUnits);
    }

    public List<ShopUnitStatisticUnit> getStatisticUnitsFromShopUnits(List<ShopUnit> shopUnits) {
        List<ShopUnitStatisticUnit> shopUnitStatisticUnits = new ArrayList<>();
        shopUnits.forEach(shopUnit -> shopUnitStatisticUnits.add(convertToStatisticUnit(shopUnit)));
        return shopUnitStatisticUnits;
    }

    public ShopUnitStatisticUnit convertToStatisticUnit(ShopUnit shopUnit) {
        ShopUnitStatisticUnit shopUnitStatisticUnit = new ShopUnitStatisticUnit();
        shopUnitStatisticUnit.setId(shopUnit.getId());
        shopUnitStatisticUnit.setName(shopUnit.getName());
        shopUnitStatisticUnit.setType(shopUnit.getType());
        shopUnitStatisticUnit.setPrice(shopUnit.getPrice());
        shopUnitStatisticUnit.setParentId(shopUnit.getParentId());
        shopUnitStatisticUnit.setDate(shopUnit.getDate());

        return shopUnitStatisticUnit;
    }

    public void mergeAllShopUnitImports(ShopUnitImportRequest shopUnitImportRequest,
                                        Map<UUID, ShopUnit> shopUnitsFromDB,
                                        Map<UUID, ShopUnit> parentShopUnitsFromDB) {
        Map<UUID, ShopUnitImport> shopUnitImports = shopUnitImportRequest.getItems()
                .stream()
                .collect(Collectors.toMap(ShopUnitImport::getId, Function.identity()));
        List<ShopUnit> shopUnitsToAdd = convertAllShopUnitImportsToShopUnits(shopUnitImports, shopUnitsFromDB,
                parentShopUnitsFromDB, shopUnitImportRequest.getUpdateDate());

        mergeAllShopUnits(shopUnitsToAdd);
    }


    public void mergeAllShopUnits(List<ShopUnit> shopUnits) {
        shopUnitRepository.saveAll(shopUnits);
    }

    public Map<UUID, ShopUnit> getAllShopUnitsById(List<ShopUnitImport> shopUnitImports) {
        List<UUID> uuids = shopUnitImports.stream().map(ShopUnitImport::getId).collect(Collectors.toList());
        List<ShopUnit> shopUnits = shopUnitRepository.findAllById(uuids);
        return shopUnits.stream().collect(Collectors.toMap(ShopUnit::getId, Function.identity()));
    }

    public Map<UUID, ShopUnit> getAllParentShopUnitsByParentId(List<ShopUnitImport> shopUnitImports) {
        List<UUID> parentUUIDs = shopUnitImports
                .stream()
                .map(ShopUnitImport::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<ShopUnit> parentShopUnits = shopUnitRepository.findAllById(parentUUIDs);
        return parentShopUnits.stream().collect(Collectors.toMap(ShopUnit::getId, Function.identity()));
    }

    public List<ShopUnit> convertAllShopUnitImportsToShopUnits(Map<UUID, ShopUnitImport> shopUnitImports,
                                                               Map<UUID, ShopUnit> shopUnitsFromDB,
                                                               Map<UUID, ShopUnit> parentShopUnitsFromDB,
                                                               Instant updateDate) {
        Map<UUID, ShopUnit> preparedShopUnits = new HashMap<>();
        for (ShopUnitImport shopUnitImport : shopUnitImports.values()) {
            if (preparedShopUnits.containsKey(shopUnitImport.getId())) {
                continue;
            }
            prepareShopUnit(preparedShopUnits, shopUnitImport, shopUnitImports,
                    shopUnitsFromDB, parentShopUnitsFromDB, updateDate);
        }
        return new ArrayList<>(preparedShopUnits.values());
    }

    public ShopUnit prepareShopUnit(Map<UUID, ShopUnit> preparedShopUnits,
                                    ShopUnitImport shopUnitImport,
                                    Map<UUID, ShopUnitImport> shopUnitImports,
                                    Map<UUID, ShopUnit> shopUnits,
                                    Map<UUID, ShopUnit> parentShopUnits,
                                    Instant updateDate) {
        if (preparedShopUnits.containsKey(shopUnitImport.getId())) {
            return preparedShopUnits.get(shopUnitImport.getId());
        }
        ShopUnit baseShopUnit = new ShopUnit();
        if (shopUnits.containsKey(shopUnitImport.getId())) {
            baseShopUnit = shopUnits.get(shopUnitImport.getId());
        }
        ShopUnit parentShopUnit = null;
        if (shopUnitImport.getParentId() != null) {
            if (parentShopUnits.containsKey(shopUnitImport.getParentId())) {
                parentShopUnit = parentShopUnits.get(shopUnitImport.getParentId());
                parentShopUnit.setDate(updateDate);
            } else {
                parentShopUnit = prepareShopUnit(preparedShopUnits, shopUnitImports.get(shopUnitImport.getParentId()),
                        shopUnitImports, shopUnits, parentShopUnits, updateDate);
            }
        }
        ShopUnit shopUnitToAdd = convertShopUnitImportToShopUnit(shopUnitImport, baseShopUnit, parentShopUnit, updateDate);
        preparedShopUnits.put(shopUnitImport.getId(), shopUnitToAdd);
        shopUnitUpdateService.saveUpdate(shopUnitToAdd);
        if (parentShopUnit != null) {
            parentShopUnit.getChildren().add(shopUnitToAdd);
            shopUnitUpdateService.saveUpdate(parentShopUnit);
        }
        return convertShopUnitImportToShopUnit(shopUnitImport, baseShopUnit, parentShopUnit, updateDate);
    }

    public ShopUnit convertShopUnitImportToShopUnit(ShopUnitImport shopUnitImport, ShopUnit shopUnit,
                                                    ShopUnit parentShopUnit, Instant updateDate) {
        shopUnit.setId(shopUnitImport.getId());
        shopUnit.setType(shopUnitImport.getType());
        shopUnit.setName(shopUnitImport.getName());
        shopUnit.setPrice(shopUnitImport.getPrice());
        shopUnit.setDate(updateDate);
        shopUnit.setParent(parentShopUnit);
        return shopUnit;
    }


}
