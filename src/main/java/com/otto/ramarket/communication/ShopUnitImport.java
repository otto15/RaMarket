package com.otto.ramarket.communication;

import com.otto.ramarket.entity.ShopUnitType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class ShopUnitImport {


    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ShopUnitType type;

    private UUID parentId;

    private Integer price;


    public ShopUnitImport() {

    }

    public ShopUnitImport(UUID id, String name, ShopUnitType type, UUID parentId, int price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parentId = parentId;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShopUnitType getType() {
        return type;
    }

    public void setType(ShopUnitType type) {
        this.type = type;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
