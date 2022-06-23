package com.otto.ramarket.controller;

import com.otto.ramarket.communication.ShopUnitImportRequest;
import com.otto.ramarket.service.ShopUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
public class ShopUnitController {

    private static final String IMPORTS_SUCCESS_MESSAGE = "The insertion or update was successful";
    private static final String DELETE_SUCCESS_MESSAGE = "The deletion was successful";

    private final ShopUnitService shopUnitService;

    @Autowired
    public ShopUnitController(ShopUnitService shopUnitService) {
        this.shopUnitService = shopUnitService;
    }


    @PostMapping("/imports")
    public String importShopUnits(@RequestBody @Validated ShopUnitImportRequest shopUnitImportRequest) {
        shopUnitService.processImport(shopUnitImportRequest);
        return IMPORTS_SUCCESS_MESSAGE;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteShopUnit(@PathVariable UUID id) {
        return DELETE_SUCCESS_MESSAGE;
    }

//    @GetMapping("/nodes/{id}")
//    public

}
