package com.otto.ramarket.communication;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public class ShopUnitImportRequest {

    @NotNull
    private final Instant updateDate;

    @NotNull
    @Valid
    private final List<ShopUnitImport> items;

    public ShopUnitImportRequest(List<ShopUnitImport> items, Instant updateDate) {
        this.items = items;
        this.updateDate = updateDate;
    }

    public List<ShopUnitImport> getItems() {
        return items;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    @Override
    public String toString() {
        return "ShopUnitImportRequest{" +
                "items=" + items +
                ", updateDate=" + updateDate +
                '}';
    }
}
