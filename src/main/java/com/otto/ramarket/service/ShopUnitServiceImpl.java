package com.otto.ramarket.service;

import com.otto.ramarket.communication.ShopUnitImport;
import com.otto.ramarket.communication.ShopUnitImportRequest;
import com.otto.ramarket.entity.ShopUnit;
import com.otto.ramarket.repository.ShopUnitRepository;
import com.otto.ramarket.validation.ImportRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShopUnitServiceImpl implements ShopUnitService {

    private final ShopUnitRepository shopUnitRepository;
    private final ImportRequestValidator importRequestValidator;

    @Autowired
    public ShopUnitServiceImpl(ShopUnitRepository shopUnitRepository, ImportRequestValidator importRequestValidator) {
        this.shopUnitRepository = shopUnitRepository;
        this.importRequestValidator = importRequestValidator;
    }


    @Override
    public void mergeShopUnit(ShopUnit shopUnit) {
        shopUnitRepository.save(shopUnit);
    }

    @Override
    public void mergeAllShopUnitsWithAddingUpdateDate(ShopUnitImportRequest shopUnitImportRequest) {
        List<ShopUnitImport> shopUnitImports = shopUnitImportRequest.getItems();
        ZonedDateTime updateDate = shopUnitImportRequest.getUpdateDate();
        List<ShopUnit> shopUnits = new ArrayList<>();
        for (ShopUnitImport shopUnitImport : shopUnitImports) {
            ShopUnit shopUnitUnderConsideration = new ShopUnit();
            shopUnitUnderConsideration.setId(shopUnitImport.getId());
            shopUnitUnderConsideration.setName(shopUnitImport.getName());
            shopUnitUnderConsideration.setPrice(shopUnitImport.getPrice());
            shopUnitUnderConsideration.setType(shopUnitImport.getType());
            shopUnitUnderConsideration.setDate(updateDate);
            if (shopUnitImport.getParentId() != null) {
                Optional<ShopUnit> optionalParent = shopUnitRepository.findById(shopUnitImport.getParentId());
                shopUnitUnderConsideration.setParent(optionalParent.orElse(null));
            } else {
                shopUnitUnderConsideration.setParent(null);
            }
            shopUnits.add(shopUnitUnderConsideration);
        }

        mergeAllShopUnits(shopUnits);
    }

    @Override
    public void mergeAllShopUnits(List<ShopUnit> shopUnits) {
        shopUnitRepository.saveAll(shopUnits);
    }

    @Override
    public void processImport(ShopUnitImportRequest shopUnitImportRequest) {
        Map<UUID, ShopUnit> shopUnitsFromDB = getAllShopUnitsByShopUnitImportsUUID(shopUnitImportRequest.getItems());

        importRequestValidator.validateImportRequest(shopUnitImportRequest, shopUnitsFromDB);
        mergeAllShopUnitsWithAddingUpdateDate(shopUnitImportRequest);
    }

    public Map<UUID, ShopUnit> getAllShopUnitsByShopUnitImportsUUID(List<ShopUnitImport> shopUnitImports) {
        List<UUID> uuids = shopUnitImports.stream().map(ShopUnitImport::getId).collect(Collectors.toList());
        List<ShopUnit> shopUnits = shopUnitRepository.findAllById(uuids);
        return shopUnits.stream().collect(Collectors.toMap(ShopUnit::getId, Function.identity()));
    }

}
