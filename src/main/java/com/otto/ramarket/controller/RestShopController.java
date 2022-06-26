package com.otto.ramarket.controller;

import com.otto.ramarket.communication.ShopUnitImportRequest;
import com.otto.ramarket.communication.ShopUnitStatisticUnit;
import com.otto.ramarket.entity.ShopUnit;
import com.otto.ramarket.service.ShopUnitService;
import com.otto.ramarket.service.ShopUnitUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class RestShopController {

    private static final String IMPORTS_SUCCESS_MESSAGE = "The insertion or update was successful";
    private static final String DELETE_SUCCESS_MESSAGE = "The deletion was successful";

    private final ShopUnitService shopUnitService;
    private final ShopUnitUpdateService shopUnitUpdateService;

    @Autowired
    public RestShopController(ShopUnitService shopUnitService, ShopUnitUpdateService shopUnitUpdateService) {
        this.shopUnitService = shopUnitService;
        this.shopUnitUpdateService = shopUnitUpdateService;
    }


    @PostMapping("/imports")
    public String importShopUnits(@RequestBody @Validated ShopUnitImportRequest shopUnitImportRequest) {
        shopUnitService.executeImport(shopUnitImportRequest);
        return IMPORTS_SUCCESS_MESSAGE;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteShopUnit(@PathVariable UUID id) {
        shopUnitService.deleteShopUnitById(id);
        return DELETE_SUCCESS_MESSAGE;
    }

    @GetMapping("/nodes/{id}")
    public ShopUnit getShopUnit(@PathVariable UUID id) {
        return shopUnitService.getShopUnitById(id);
    }

    @GetMapping("/sales")
    public List<ShopUnitStatisticUnit> getShopUnitStatisticUnits(@RequestParam Instant date) {
        return shopUnitService.getShopUnitsUpdatedInLast24HoursFromGivenDate(date);
    }

    @GetMapping("/node/{id}/statistic")
    public List<ShopUnitStatisticUnit> getStatisticsForTheShopUnit(@PathVariable UUID id,
                                                                   @RequestParam Instant dateStart,
                                                                   @RequestParam Instant dateEnd) {
        return shopUnitUpdateService.getStatsForTheShopUnit(id, dateStart, dateEnd);
    }

}
