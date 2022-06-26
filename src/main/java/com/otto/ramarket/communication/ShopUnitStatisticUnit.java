package com.otto.ramarket.communication;

import com.otto.ramarket.entity.ShopUnitType;

import java.time.Instant;
import java.util.UUID;

public class ShopUnitStatisticUnit extends ShopUnitImport {

    private Instant date;

    public ShopUnitStatisticUnit() {
        super();
    }

    public ShopUnitStatisticUnit(UUID id, String name, ShopUnitType type, UUID parentId, int price, Instant date) {
        super(id, name, type, parentId, price);
        this.date = date;
    }


    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ShopUnitStatisticUnit{" +
                "id=" + getId() +
                "lastUpdateDate=" + date +
                '}';
    }
}
