package com.otto.ramarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "shop_units")
public class ShopUnit {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;

    @Column(name = "date", nullable = false)
    private Instant date;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ShopUnit parent;

    @Column(name = "parent_id", updatable = false, insertable = false)
    private UUID parentId;

    @Column(name = "price")
    private Integer price;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<ShopUnit> children = new HashSet<>();

    @OneToMany(mappedBy = "shopUnit", cascade = CascadeType.ALL)
    private List<ShopUnitUpdate> updates;

    public ShopUnit() {

    }

    public ShopUnit(UUID id, String name, ShopUnitType type, Instant date, Integer price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.date = date;
        this.price = price;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
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

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        if (this.date != null && date.compareTo(this.date) < 0) {
            return;
        }
        if (this.parentId != null) {
            this.parent.setDate(date);
        }
        this.date = date;
    }

    public ShopUnit getParent() {
        return parent;
    }

    public void setParent(ShopUnit parent) {
        this.parent = parent;
    }

    public Integer getPrice() {
        if (this.getType() == ShopUnitType.CATEGORY) {
            return calculatePriceForCategory();
        }
        return price;
    }

    public Integer calculatePriceForCategory() {
        Queue<ShopUnit> shopUnitsQueue = new LinkedList<>();
        shopUnitsQueue.add(this);
        int averagePrice = 0;
        int countOfOffers = 0;
        while (!shopUnitsQueue.isEmpty()) {
            ShopUnit unit = shopUnitsQueue.poll();
            shopUnitsQueue.addAll(unit.children);
            if (unit.getType() == ShopUnitType.OFFER) {
                countOfOffers++;
                averagePrice += unit.getPrice();
            }
        }
        if (countOfOffers == 0) {
            return null;
        }
        return averagePrice / countOfOffers;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Set<ShopUnit> getChildren() {
        if (this.getType() == ShopUnitType.OFFER && this.children.isEmpty()) {
            return null;
        }
        return children;
    }

    public void setChildren(Set<ShopUnit> children) {
        this.children = children;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "ShopUnit{" +
                "id=" + id +
                ", name='" + name +
                ", type=" + type +
                ", date=" + date +
                ", price=" + price +
                '}';
    }

}
