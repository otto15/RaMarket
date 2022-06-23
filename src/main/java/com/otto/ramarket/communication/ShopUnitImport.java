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

    private Long price;



    public ShopUnitImport() {

    }

    public ShopUnitImport(UUID id, String name, ShopUnitType type, UUID parentId, Long price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parentId = parentId;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ShopUnitType getType() {
        return type;
    }

    public UUID getParentId() {
        return parentId;
    }

    public Long getPrice() {
        return price;
    }
}
