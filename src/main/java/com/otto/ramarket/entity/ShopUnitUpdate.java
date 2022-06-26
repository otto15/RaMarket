package com.otto.ramarket.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "shop_unit_updates")
public class ShopUnitUpdate {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "shop_unit_id")
    private ShopUnit shopUnit;

    @Column(name = "shop_unit_id", updatable = false, insertable = false)
    @JsonProperty(value = "id")
    private UUID shopUnitId;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "price")
    private Integer price;

    @Column(name = "name", nullable = false)
    private String name;

    public ShopUnitUpdate() {

    }

    public ShopUnitUpdate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public ShopUnit getShopUnit() {
        return shopUnit;
    }

    public void setShopUnit(ShopUnit shopUnit) {
        this.shopUnit = shopUnit;
    }



    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ShopUnitUpdate{" +
                "updateDate=" + updateDate +
                '}';
    }

}
